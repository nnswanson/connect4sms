import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.verbs.TwiMLResponse;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


public class TwilioServlet extends HttpServlet {

    private static final String ACCOUNT_SID = "ACf9535986e1e6c6c572603d406fd01169";
    private static final String AUTH_TOKEN = "e54b9bc44d828066cab351eccbd870a0";

    ArrayList<Game> games;
    String waitingPlayer;

    private Game getGame(String playerNumber) {
        for (Game g : this.games) {
            if (g.hasPlayer(playerNumber))
                return g;
        }
        return null;
    }

    public void init() throws ServletException {
        this.games = new ArrayList<>();
    }


    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String fromNumber = request.getParameter("From");
        String toNumber = request.getParameter("To");
        String body = request.getParameter("Body");

        Game game = getGame(fromNumber);
        String message = "";
        if (game == null) {
            if (body.toLowerCase().equals("join")) {

                if (this.waitingPlayer == null) {
                    this.waitingPlayer = fromNumber;
                    message = "Waiting for another player to join...";
                } else {
                    if (this.waitingPlayer.equals(fromNumber)) {
                        message = "You are already waiting to join a game. Please be patient :)";
                    } else {
                        this.games.add(new Game(this.waitingPlayer, fromNumber));
                        this.waitingPlayer = null;
                        message = "You have successfully joined a game! ^_^ When it's your turn, send a column number to place a piece.";
                        sendMessage(toNumber, game.players.get(game.currentTurn), message + "\nIt's your turn!");
                        sendMessage(toNumber, game.players.get((game.currentTurn + 1) % 2), message);
                    }
                }
            } else {
                message = "You aren't in a game yet silly! Send 'join' to enter a game.";
            }
        } else {
            // if fromNumber is current player's turn, check if they sent an int
            if (fromNumber.equals(game.players.get(game.currentTurn))) {
                try {
                    int column = Integer.parseInt(body);
                    if (game.board.canPlacePiece(column)) {
                        game.board.placePiece(game.currentTurn, column);
                        int winner = game.board.getWinner();
                        if (winner == 0) {
                            game.currentTurn = (game.currentTurn + 1) % 2;
                            message = "Current board:\n";
                            message += game.board.toString();
                            // send to other person too
                            sendMessage(toNumber, game.players.get(game.currentTurn), message + "\nIt's your turn!");
                        } else { // game over
                            message = "Game over! Player " + (game.currentTurn + 1) + " wins!";
                            this.games.remove(game);
                        }
                    } else {
                        message = "Invalid move. Please send a valid column number.";
                    }
                } catch(Exception e) { // if no int was sent/ not valid int, reply with invalid message.
                    message = "Invalid command. Please send a valid column number.";
                }
            } else { // if fromNumber != current player's turn, reply with "it's not ur turn"
                message = "It's not your turn.";
            }


        }

        sendMessage(toNumber, fromNumber, message);
    }

    public void sendMessage(String fromNumber, String toNumber, String body) {
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", toNumber));
        params.add(new BasicNameValuePair("From", fromNumber));
        params.add(new BasicNameValuePair("Body", body));

        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        try {
            Message message = messageFactory.create(params);
        } catch (TwilioRestException e) {
            System.out.println(e.getErrorMessage());
        }
    }
}