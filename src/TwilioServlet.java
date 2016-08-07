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

        if (body.equals("?")) {
            sendMessage(toNumber, fromNumber,
                    "Connect4SMS Help: Send 'join' to enter a game. Once you enter a game, send a column number to place a piece in that column. Get 4 in a row vertically, horizontally, or diagonally to win.");
        }

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
                        message = "You have successfully joined a game! ^_^ When it's your turn, send a column number to place a piece.";
                                //+ " Board:\n" + game.board.toString();
                        sendMessage(toNumber, this.waitingPlayer, message + "\nIt's your turn, Player 1!");
                        sendMessage(toNumber, fromNumber, message + "\nIt's not your turn, Player 2 :(");
                        this.waitingPlayer = null;
                        return;
                    }
                }
            } else {
                message = "You aren't in a game yet silly! Send 'join' to enter a game or send '?' for help";
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
                            message = "\nPlayer " + (game.currentTurn + 1) + " placed a piece in column " + column;
                            game.currentTurn = (game.currentTurn + 1) % 2;

                            message += "\nCurrent board:\n";
                            message += game.board.toString();
                            // send to other person too
                            sendMessage(toNumber, game.players.get(game.currentTurn), message + "\nIt's your turn!");
                        } else { // game over
                            message = "Current board:\n" + game.board.toString() + "Game over! Player " + (game.currentTurn + 1) + " wins!";
                            // send to other person too
                            sendMessage(toNumber, game.players.get(game.currentTurn), message);
                            sendMessage(toNumber, game.players.get((game.currentTurn + 1) % 2), message);
                            this.games.remove(game);
                            return;
                        }
                    } else {
                        message = "Invalid move. Please send a valid column number. Send '?' for help.";
                    }
                } catch(Exception e) { // if no int was sent/ not valid int, reply with invalid message.
                    message = "Invalid command. Please send a valid column number. Send '?' for help";
                }
            } else { // if fromNumber != current player's turn, reply with "it's not ur turn"
                message = "It's not your turn. Send '?' for help.";
            }


        }

        sendMessage(toNumber, fromNumber, message);
    }

    public void sendMessage(String fromNumber, String toNumber, String body) {
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("To", toNumber));
        params.add(new BasicNameValuePair("From", fromNumber));
        params.add(new BasicNameValuePair("Body", body));

        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        try {
            Message message = messageFactory.create(params);
            System.out.println(message.getSid());
        } catch (TwilioRestException e) {
            System.out.println(e.getErrorMessage());
        }
    }
}