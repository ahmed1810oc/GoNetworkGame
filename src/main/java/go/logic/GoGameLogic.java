package go.logic;

import go.model.GameState;
import go.model.Stone;

/**
 * Main logic controller for the Go game.
 */
public class GoGameLogic {

    private GameState gameState;
    private MoveValidator moveValidator;

    public GoGameLogic(GameState gameState) {
        this.gameState = gameState;
        this.moveValidator = new MoveValidator();
    }

    /**
     * Tries to place a stone for the current player.
     */
    public boolean playMove(int row, int col, Stone playerStone) {
        if (gameState.isGameOver()) {
            return false;
        }

        if (playerStone != gameState.getCurrentTurn()) {
            return false;
        }

        boolean valid = moveValidator.isValidMove(gameState.getBoard(), row, col);

        if (!valid) {
            return false;
        }

        gameState.getBoard().placeStone(row, col, playerStone);

        // Since this was a real move, reset pass tracking.
        gameState.setLastMoveWasPass(false);

        gameState.switchTurn();

        return true;
    }

    /**
     * Handles pass action.
     * Returns true if the game ends because both players passed consecutively.
     */
    public boolean passTurn(Stone playerStone) {
        if (gameState.isGameOver()) {
            return false;
        }

        if (playerStone != gameState.getCurrentTurn()) {
            return false;
        }

        if (gameState.wasLastMovePass()) {
            gameState.endGame();
            return true;
        }

        gameState.setLastMoveWasPass(true);
        gameState.switchTurn();

        return false;
    }

    /**
     * Counts stones and returns the winner.
     * This is a simplified scoring system for the project.
     */
    public Stone calculateWinner() {
        int blackCount = 0;
        int whiteCount = 0;

        for (int row = 0; row < gameState.getBoard().SIZE; row++) {
            for (int col = 0; col < gameState.getBoard().SIZE; col++) {
                Stone stone = gameState.getBoard().getStone(row, col);

                if (stone == Stone.BLACK) {
                    blackCount++;
                } else if (stone == Stone.WHITE) {
                    whiteCount++;
                }
            }
        }

        if (blackCount > whiteCount) {
            return Stone.BLACK;
        } else if (whiteCount > blackCount) {
            return Stone.WHITE;
        } else {
            return Stone.EMPTY; // draw
        }
    }

    public int countStones(Stone targetStone) {
        int count = 0;

        for (int row = 0; row < gameState.getBoard().SIZE; row++) {
            for (int col = 0; col < gameState.getBoard().SIZE; col++) {
                if (gameState.getBoard().getStone(row, col) == targetStone) {
                    count++;
                }
            }
        }

        return count;
    }
}