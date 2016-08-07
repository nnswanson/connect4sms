import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by Nick on 8/6/2016.
 */
public class Game implements Runnable {

    Board board;
    Dictionary<String, Integer> players;


    public Game(String player1, String player2) {
        this.board = new Board();
        this.players = new Hashtable<>();
        this.players.put(player1, 1);
        this.players.put(player2, 2);
    }

    public void run() {
        int winner = 0;
        int move = -1;
        int currentPlayer = 1;
        while (winner == 0) {
            while (true) {
                // TODO: wait for text from current player
                // int move =

                if (this.board.canPlacePiece(move))
                    break;

                // TODO: Text player that their move was invalid
            }

            this.board.placePiece(currentPlayer, move);
            // TODO: Text players the move
            currentPlayer = currentPlayer == 1 ? 2 : 1; // Switch player after turn


            winner = this.board.getWinner();
        }

        // Text players that game is over
    }
}
