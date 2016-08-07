import com.sun.org.apache.xpath.internal.SourceTree;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Nick on 8/6/2016.
 */
public class Game implements Runnable {

    public static final String ACCOUNT_SID = "account_sid";
    public static final String AUTH_TOKEN = "auth_token";

    Board board;
    ArrayList<String> players;
    TwilioRestClient client;

    public Game(String player1, String player2) {
        this.board = new Board();
        this.players = new ArrayList<>();
        this.players.add(player1);
        this.players.add(player2);
        this.client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void run() {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        int winner = 0;
        int move;
        int currentPlayer = 1;
        while (winner == 0) {
            while (true) {
                // TODO: wait for text from current player
                // int move =
                System.out.println("Please enter a move, Player " + currentPlayer + ": ");
                move = reader.nextInt() - 1; // Subtract 1 for zero-based indexing


                if (this.board.canPlacePiece(move))
                    break;

                /*
                // TODO: Text player that their move was invalid
                List<NameValuePair> messageParams = new ArrayList<>();
                messageParams.add(new BasicNameValuePair("Body", "That move is not valid. Please enter a valid move."));
                messageParams.add(new BasicNameValuePair("To", this.players.get(currentPlayer - 1)));
                messageParams.add(new BasicNameValuePair("From", this.players.get(1)));

                MessageFactory messageFactory = this.client.getAccount().getMessageFactory();
                try {
                    Message message = messageFactory.create(messageParams);
                    System.out.println(message.getSid());
                } catch (Exception e) {}
                */
                System.out.println("Move was invalid");

            }

            this.board.placePiece(currentPlayer, move);
            // TODO: Text players the move
            System.out.println("Player " + currentPlayer + " placed a piece at column " + move);
            System.out.println("The board now looks like: \n" + this.board.toString());

            currentPlayer = currentPlayer == 1 ? 2 : 1; // Switch player after turn


            winner = this.board.getWinner();
        }

        // Text players that game is over
        System.out.println("Winner: " + winner);
    }
}
