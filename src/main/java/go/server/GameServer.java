package go.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main server class.
 * It waits for two clients and then starts a game session.
 */
public class GameServer {

    private int port;

    public GameServer(int port) {
        this.port = port;
    }

    public void start() {
        System.out.println("Starting Go Game Server...");
        System.out.println("Port: " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is running. Waiting for Player 1...");

            Socket player1Socket = serverSocket.accept();
            System.out.println("Player 1 connected: " + player1Socket.getInetAddress());

            System.out.println("Waiting for Player 2...");

            Socket player2Socket = serverSocket.accept();
            System.out.println("Player 2 connected: " + player2Socket.getInetAddress());

            GameSession session = new GameSession(player1Socket, player2Socket);
            session.start();

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}