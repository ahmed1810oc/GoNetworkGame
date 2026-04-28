package go.logic;

import go.model.Board;
import go.model.Stone;

/**
 * Checks whether a move is legal.
 * For now, we only check simple rules.
 */
public class MoveValidator {

    public boolean isValidMove(Board board, int row, int col) {
        if (!board.isInsideBoard(row, col)) {
            return false;
        }

        if (board.getStone(row, col) != Stone.EMPTY) {
            return false;
        }

        return true;
    }
}