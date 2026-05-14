package go.logic;

import go.model.Board;
import go.model.GameState;
import go.model.Stone;

/**
 * Main logic controller for the Go game.
 */
public class GoGameLogic {

    private GameState gameState;
    private MoveValidator moveValidator;
    private CaptureChecker captureChecker;
    private int lastCapturedCount;

    public GoGameLogic(GameState gameState) {
        this.gameState = gameState;
        this.moveValidator = new MoveValidator();
        this.captureChecker = new CaptureChecker();
        this.lastCapturedCount = 0;
    }

    /**
     * Tries to place a stone for the current player.
     */
    public boolean playMove(int row, int col, Stone playerStone) {
        lastCapturedCount = 0;

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

        Stone[][] oldBoard = gameState.getBoard().getGridCopy();

        gameState.getBoard().placeStone(row, col, playerStone);

        lastCapturedCount = captureChecker.removeCapturedOpponentStones(
                gameState.getBoard(),
                row,
                col,
                playerStone
        );

        boolean ownGroupHasLiberty = captureChecker.groupHasLiberty(
                gameState.getBoard(),
                row,
                col
        );

        if (!ownGroupHasLiberty) {
            gameState.getBoard().setGrid(oldBoard);
            lastCapturedCount = 0;
            return false;
        }

        gameState.addCapturedStones(playerStone, lastCapturedCount);

        gameState.setLastMoveWasPass(false);
        gameState.switchTurn();

        return true;
    }

    public int getLastCapturedCount() {
        return lastCapturedCount;
    }

    /**
     * Handles pass action. Returns true if the game ends because both players
     * passed consecutively.
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
     * Counts stones and returns the winner. This is a simplified scoring system
     * for the project.
     */
    public Stone calculateWinner() {
        int blackCount = countStones(Stone.BLACK);
        int whiteCount = countStones(Stone.WHITE);

        if (blackCount > whiteCount) {
            return Stone.BLACK;
        } else if (whiteCount > blackCount) {
            return Stone.WHITE;
        } else {
            return Stone.EMPTY;
        }
    }

    public int countStones(Stone targetStone) {
        int count = 0;

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (gameState.getBoard().getStone(row, col) == targetStone) {
                    count++;
                }
            }
        }

        return count;
    }
}
