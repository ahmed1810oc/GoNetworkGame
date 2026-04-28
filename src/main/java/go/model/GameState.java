package go.model;

/**
 * Stores the current state of one Go game.
 */
public class GameState {

    private Board board;
    private Stone currentTurn;
    private boolean gameStarted;
    private boolean gameOver;

    public GameState() {
        board = new Board();
        currentTurn = Stone.BLACK;
        gameStarted = false;
        gameOver = false;
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

    public void startGame() {
        gameStarted = true;
        gameOver = false;
        currentTurn = Stone.BLACK;
        board.clearBoard();
    }

    public void endGame() {
        gameOver = true;
    }

    public void switchTurn() {
        currentTurn = currentTurn.opposite();
    }
}