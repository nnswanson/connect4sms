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
public class Game {

    Board board;
    ArrayList<String> players;
    int currentTurn;

    public Game(String player1, String player2) {
        this.board = new Board();
        this.players = new ArrayList<>();
        this.players.add(player1);
        this.players.add(player2);
        this.currentTurn = 0;
    }

    public boolean hasPlayer(String player) {
        return this.players.contains(player);
    }
}
