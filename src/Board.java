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
        while(this.board.get(column).get(row) != 0) {
            row++;
        }
        this.board.get(column).add(row, player);
    }

    // if return false, can't place piece
    public boolean canPlacePiece(int column) {
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
        for (int rowNum = 0; rowNum < NUM_OF_ROWS; rowNum++) {
            count = 0;
            for (int colNum = 0; colNum < NUM_OF_COLUMNS; colNum++) {
                if (checkWinner(count, currentValue, colNum, rowNum))
                    return currentValue;
            }
        }
        return 0;
    }

    private int getWinnerColumns() {
        int count;
        int currentValue = 0;
        for (int colNum = 0; colNum < NUM_OF_COLUMNS; colNum++) {
            count = 0;
            for (int rowNum = 0; rowNum < NUM_OF_ROWS; rowNum++) {
                if (checkWinner(count, currentValue, colNum, rowNum))
                    return currentValue;
            }
        }
        return 0;
    }

    private int getWinnerDiagonals() {
        return 0; // TODO: this
    }

    private boolean checkWinner(int count, int currentValue, int colNum, int rowNum) {
        int currentCell = this.board.get(colNum).get(rowNum);
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
                return true;
            }
        }
        return false;
    }

    public String toString() {
        String s = "";

        return s;
    }
}
