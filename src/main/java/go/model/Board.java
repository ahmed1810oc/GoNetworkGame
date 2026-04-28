package go.model;

/**
 * Represents the Go board.
 * We will use a 9x9 board to keep the project simple and manageable.
 */
public class Board {

    public static final int SIZE = 9;

    private Stone[][] grid;

    public Board() {
        grid = new Stone[SIZE][SIZE];
        clearBoard();
    }

    /**
     * Sets all cells to EMPTY.
     */
    public void clearBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col] = Stone.EMPTY;
            }
        }
    }

    /**
     * Returns the stone at a specific position.
     */
    public Stone getStone(int row, int col) {
        if (!isInsideBoard(row, col)) {
            return null;
        }

        return grid[row][col];
    }

    /**
     * Places a stone on the board.
     */
    public boolean placeStone(int row, int col, Stone stone) {
        if (!isInsideBoard(row, col)) {
            return false;
        }

        if (grid[row][col] != Stone.EMPTY) {
            return false;
        }

        grid[row][col] = stone;
        return true;
    }

    /**
     * Removes a stone from the board.
     * This will be useful later for capture logic.
     */
    public void removeStone(int row, int col) {
        if (isInsideBoard(row, col)) {
            grid[row][col] = Stone.EMPTY;
        }
    }

    /**
     * Checks if a position is inside the board limits.
     */
    public boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    /**
     * Returns a copy of the board grid.
     */
    public Stone[][] getGridCopy() {
        Stone[][] copy = new Stone[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                copy[row][col] = grid[row][col];
            }
        }

        return copy;
    }

    /**
     * Prints the board in console.
     * Useful for testing before GUI.
     */
    public void printBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == Stone.EMPTY) {
                    System.out.print(". ");
                } else if (grid[row][col] == Stone.BLACK) {
                    System.out.print("B ");
                } else {
                    System.out.print("W ");
                }
            }
            System.out.println();
        }
    }
}
