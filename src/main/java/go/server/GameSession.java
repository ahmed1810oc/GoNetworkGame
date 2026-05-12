package go.server;

import go.logic.GoGameLogic;
import go.model.GameState;
import go.model.Stone;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Represents one game session between two connected clients.
 */
public class GameSession {

    private Socket player1Socket;
    private Socket player2Socket;

    private PrintWriter player1Out;
    private PrintWriter player2Out;

    private Scanner player1In;
    private Scanner player2In;

    private GameState gameState;
    private GoGameLogic gameLogic;

    public GameSession(Socket player1Socket, Socket player2Socket) {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;

        this.gameState = new GameState();
        this.gameLogic = new GoGameLogic(gameState);
    }

    public void start() {
        try {
            player1Out = new PrintWriter(player1Socket.getOutputStream(), true);
            player2Out = new PrintWriter(player2Socket.getOutputStream(), true);

            player1In = new Scanner(player1Socket.getInputStream());
            player2In = new Scanner(player2Socket.getInputStream());

            player1Out.println("ASSIGN BLACK");
            player2Out.println("ASSIGN WHITE");

            gameState.startGame();

            broadcast("START");
            broadcast("TURN BLACK");

            System.out.println("Game session started.");

            listenToPlayers();

        } catch (IOException e) {
            System.out.println("Game session error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listenToPlayers() {
        Thread player1Thread = new Thread(() -> listenToPlayer(player1In, Stone.BLACK));
        Thread player2Thread = new Thread(() -> listenToPlayer(player2In, Stone.WHITE));

        player1Thread.start();
        player2Thread.start();
    }

    private void listenToPlayer(Scanner input, Stone playerStone) {
        while (input.hasNextLine()) {
            String message = input.nextLine();

            System.out.println(playerStone + " sent: " + message);

            handleMessage(message, playerStone);
        }
    }

    private synchronized void handleMessage(String message, Stone playerStone) {
        if (message.startsWith("MOVE")) {
            handleMove(message, playerStone);
        } else if (message.startsWith("PASS")) {
            handlePass(playerStone);
        } else if (message.startsWith("RESTART_REQUEST")) {
            handleRestartRequest(playerStone);
        } else if (message.startsWith("NAME")) {
            handleName(message, playerStone);
        } else {
            sendToPlayer(playerStone, "ERROR Unknown command");
        }
    }

    private void handleMove(String message, Stone playerStone) {
        String[] parts = message.split(" ");

        if (parts.length != 3) {
            sendToPlayer(playerStone, "ERROR Invalid MOVE format. Use: MOVE row col");
            return;
        }

        try {
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);

            boolean success = gameLogic.playMove(row, col, playerStone);

            if (success) {
                broadcast("BOARD " + boardToProtocolString());
                broadcast("TURN " + gameState.getCurrentTurn());
                gameState.getBoard().printBoard();
            } else {
                sendToPlayer(playerStone, "INVALID_MOVE");
            }

        } catch (NumberFormatException e) {
            sendToPlayer(playerStone, "ERROR Row and column must be numbers");
        }
    }

    private void sendToPlayer(Stone playerStone, String message) {
        if (playerStone == Stone.BLACK) {
            player1Out.println(message);
        } else if (playerStone == Stone.WHITE) {
            player2Out.println(message);
        }
    }

    private void broadcast(String message) {
        player1Out.println(message);
        player2Out.println(message);
    }

    private void handleName(String message, Stone playerStone) {
        String name = message.substring(5).trim();

        if (name.isEmpty()) {
            name = "Player";
        }

        System.out.println(playerStone + " name is: " + name);
        broadcast("MESSAGE " + playerStone + " joined as " + name);
    }

    private void handlePass(Stone playerStone) {
        boolean gameEnded = gameLogic.passTurn(playerStone);

        if (gameEnded) {
            Stone winner = gameLogic.calculateWinner();

            int blackScore = gameLogic.countStones(Stone.BLACK);
            int whiteScore = gameLogic.countStones(Stone.WHITE);

            broadcast("GAME_OVER " + winner + " " + blackScore + " " + whiteScore);

            System.out.println("Game over.");
            System.out.println("Winner: " + winner);
            System.out.println("Black score: " + blackScore);
            System.out.println("White score: " + whiteScore);
        } else {
            broadcast("PASS " + playerStone);
            broadcast("TURN " + gameState.getCurrentTurn());
        }
    }

    private void handleRestartRequest(Stone playerStone) {
        gameState.addRestartRequest();

        broadcast("MESSAGE " + playerStone + " wants to play again.");

        if (gameState.getRestartRequests() >= 2) {
            gameState.startGame();

            broadcast("RESTART");
            broadcast("TURN BLACK");

            System.out.println("Game restarted.");
        } else {
            sendToPlayer(playerStone, "MESSAGE Waiting for other player to restart...");
        }
    }

    private String boardToProtocolString() {
        StringBuilder builder = new StringBuilder();

        for (int row = 0; row < go.model.Board.SIZE; row++) {
            for (int col = 0; col < go.model.Board.SIZE; col++) {
                Stone stone = gameState.getBoard().getStone(row, col);

                if (stone == Stone.BLACK) {
                    builder.append("B");
                } else if (stone == Stone.WHITE) {
                    builder.append("W");
                } else {
                    builder.append(".");
                }
            }

            if (row < go.model.Board.SIZE - 1) {
                builder.append("|");
            }
        }

        return builder.toString();
    }
}
