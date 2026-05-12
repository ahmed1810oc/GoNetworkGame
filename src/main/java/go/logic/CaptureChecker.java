package go.logic;

import go.model.Board;
import go.model.Stone;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles Go capture logic.
 * A stone/group is captured if it has no liberties.
 */
public class CaptureChecker {

    private final int[][] directions = {
            {-1, 0}, // up
            {1, 0},  // down
            {0, -1}, // left
            {0, 1}   // right
    };

    /**
     * Removes opponent groups that lost all liberties after a move.
     * Returns number of removed stones.
     */
    public int removeCapturedOpponentStones(Board board, int row, int col, Stone placedStone) {
        Stone opponent = placedStone.opposite();
        int capturedCount = 0;

        for (int[] direction : directions) {
            int nextRow = row + direction[0];
            int nextCol = col + direction[1];

            if (!board.isInsideBoard(nextRow, nextCol)) {
                continue;
            }

            if (board.getStone(nextRow, nextCol) == opponent) {
                boolean[][] visited = new boolean[Board.SIZE][Board.SIZE];
                List<int[]> group = new ArrayList<>();

                boolean hasLiberty = collectGroupAndCheckLiberty(
                        board,
                        nextRow,
                        nextCol,
                        opponent,
                        visited,
                        group
                );

                if (!hasLiberty) {
                    for (int[] position : group) {
                        board.removeStone(position[0], position[1]);
                        capturedCount++;
                    }
                }
            }
        }

        return capturedCount;
    }

    /**
     * Checks whether the group containing row,col has at least one liberty.
     */
    public boolean groupHasLiberty(Board board, int row, int col) {
        Stone stone = board.getStone(row, col);

        if (stone == null || stone == Stone.EMPTY) {
            return true;
        }

        boolean[][] visited = new boolean[Board.SIZE][Board.SIZE];
        List<int[]> group = new ArrayList<>();

        return collectGroupAndCheckLiberty(board, row, col, stone, visited, group);
    }

    /**
     * Collects all connected stones of the same color and checks if the group has a liberty.
     */
    private boolean collectGroupAndCheckLiberty(
            Board board,
            int row,
            int col,
            Stone targetStone,
            boolean[][] visited,
            List<int[]> group
    ) {
        if (!board.isInsideBoard(row, col)) {
            return false;
        }

        if (visited[row][col]) {
            return false;
        }

        visited[row][col] = true;

        Stone currentStone = board.getStone(row, col);

        if (currentStone == Stone.EMPTY) {
            return true;
        }

        if (currentStone != targetStone) {
            return false;
        }

        group.add(new int[]{row, col});

        boolean hasLiberty = false;

        for (int[] direction : directions) {
            int nextRow = row + direction[0];
            int nextCol = col + direction[1];

            if (board.isInsideBoard(nextRow, nextCol)) {
                Stone neighborStone = board.getStone(nextRow, nextCol);

                if (neighborStone == Stone.EMPTY) {
                    hasLiberty = true;
                } else if (neighborStone == targetStone && !visited[nextRow][nextCol]) {
                    boolean neighborHasLiberty = collectGroupAndCheckLiberty(
                            board,
                            nextRow,
                            nextCol,
                            targetStone,
                            visited,
                            group
                    );

                    if (neighborHasLiberty) {
                        hasLiberty = true;
                    }
                }
            }
        }

        return hasLiberty;
    }
}