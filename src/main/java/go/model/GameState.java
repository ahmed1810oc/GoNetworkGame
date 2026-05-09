package go.model;

/**
 * Stores the current state of one Go game.
 */
public class GameState {

    private Board board;
    private Stone currentTurn;
    private boolean gameStarted;
    private boolean gameOver;
    private boolean lastMoveWasPass;
    private int restartRequests;
    

    public GameState() {
        board = new Board();
        currentTurn = Stone.BLACK;
        gameStarted = false;
        gameOver = false;
        lastMoveWasPass = false;
        restartRequests = 0;
    }

    public Board getBoard() {
        return board;
    }

    public Stone getCurrentTurn() {
        return currentTurn;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean wasLastMovePass() {
        return lastMoveWasPass;
    }

    public void setLastMoveWasPass(boolean lastMoveWasPass) {
        this.lastMoveWasPass = lastMoveWasPass;
    }

    public void startGame() {
        gameStarted = true;
        gameOver = false;
        currentTurn = Stone.BLACK;
        lastMoveWasPass = false;
        restartRequests = 0;
        board.clearBoard();
    }

    public void endGame() {
        gameOver = true;
    }

    public void switchTurn() {
        currentTurn = currentTurn.opposite();
    }

    public void addRestartRequest() {
        restartRequests++;
    }

    public int getRestartRequests() {
        return restartRequests;
    }

    public void resetRestartRequests() {
        restartRequests = 0;
    }
}