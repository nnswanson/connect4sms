/**
 * Created by Nick on 8/7/2016.
 */
public class Main {
    public static void main(String[] args) {
        Game g = new Game("Player 1", "Player 2");
        Thread t = new Thread(g);
        t.start();
    }
}
