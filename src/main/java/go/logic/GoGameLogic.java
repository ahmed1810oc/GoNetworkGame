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
        gameState.switchTurn();

        return true;
    }
}