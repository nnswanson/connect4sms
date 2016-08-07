import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nick on 8/6/2016.
 */
public class Board {
    private static final int NUM_OF_COLUMNS= 7;
    private static final int NUM_OF_ROWS = 6;

    // Columns<Rows<cells>>
    private List<List<Integer>> board;

    public Board() {
        this.board = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            this.board.add(new ArrayList<>(Arrays.asList(0,0,0,0,0,0)));
        }
    }

    public void placePiece(int player, int column) {
        int row = 0;
        player++;
        column--;
        while(this.board.get(column).get(row) != 0) {
            row++;
        }
        this.board.get(column).add(row, player);
    }

    // if return false, can't place piece
    public boolean canPlacePiece(int column) {
        column--; // For 0-based indexing
        if (column >= NUM_OF_COLUMNS)
            return false;
        return this.board.get(column).get(NUM_OF_ROWS - 1) == 0;
    }

    public int getWinner() {
        int winner;

        winner = getWinnerRows();
        if (winner != 0)
            return winner;

        winner = getWinnerColumns();
        if (winner != 0)
            return winner;

        return 0;
    }

    private int getWinnerRows() {
        int count;
        int currentValue = 0;
        int currentCell;
        for (int rowNum = 0; rowNum < NUM_OF_ROWS; rowNum++) {
            count = 0;
            for (int colNum = 0; colNum < NUM_OF_COLUMNS; colNum++) {
                currentCell = this.board.get(colNum).get(rowNum);
                if (currentCell == 0) {
                    currentValue = 0;
                    count = 0;
                } else {
                    if (currentValue != currentCell) {
                        currentValue = currentCell;
                        count = 0;
                    }
                    count++;
                    if (count >= 4) {
                        return currentCell;
                    }
                }
            }
        }
        return 0;
    }

    private int getWinnerColumns() {
        int count;
        int currentValue = 0;
        int currentCell;
        for (int colNum = 0; colNum < NUM_OF_COLUMNS; colNum++) {
            count = 0;
            for (int rowNum = 0; rowNum < NUM_OF_ROWS; rowNum++) {
                currentCell = this.board.get(colNum).get(rowNum);
                if (currentCell == 0) {
                    currentValue = 0;
                    count = 0;
                } else {
                    if (currentValue != currentCell) {
                        currentValue = currentCell;
                        count = 0;
                    }
                    count++;
                    if (count >= 4) {
                        return currentCell;
                    }
                }
            }
        }
        return 0;
    }

    private int getWinnerDiagonals() {
        return 0; // TODO: this
    }

    public String toString() {
        String s = "";
        for (int row = NUM_OF_ROWS - 1; row >= 0; row--) {
            for (int col = 0; col < NUM_OF_COLUMNS; col++) {
                s += this.board.get(col).get(row) + " ";
            }
            s += "\n";
        }

        return s;
    }
}
