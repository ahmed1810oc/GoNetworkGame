package go.ui;

import go.client.NetworkClient;
import go.model.Stone;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 * Main game window for the client.
 */
public class GameScreen extends JFrame {

    private NetworkClient networkClient;
    private BoardPanel boardPanel;
    private JLabel statusLabel;

    private Stone myStone;
    private Stone currentTurn;

    private String playerName;

    public GameScreen(String serverIp, int port, String playerName) {
        this.playerName = playerName;
        networkClient = new NetworkClient();

        setupWindow();
        connectToServer(serverIp, port);
        startListeningToServer();
    }

    private void setupWindow() {
        setTitle("Go Network Game");
        setSize(650, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        statusLabel = new JLabel("Connecting to server...");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        boardPanel = new BoardPanel();

        boardPanel.setBoardClickListener((row, col) -> {
            if (myStone == null || currentTurn == null) {
                return;
            }

            if (myStone != currentTurn) {
                JOptionPane.showMessageDialog(this, "It is not your turn.");
                return;
            }

            networkClient.sendMessage("MOVE " + row + " " + col);
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(statusLabel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void connectToServer(String serverIp, int port) {
        try {
            networkClient.connect(serverIp, port);
            networkClient.sendMessage("NAME " + playerName);
            statusLabel.setText("Connected. Waiting for another player...");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not connect to server: " + e.getMessage());
            statusLabel.setText("Connection failed.");
        }
    }

    private void startListeningToServer() {
        Thread listenerThread = new Thread(() -> {
            while (true) {
                String message = networkClient.readMessage();

                if (message != null) {
                    handleServerMessage(message);
                }
            }
        });

        listenerThread.start();
    }

    private void handleServerMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("SERVER: " + message);

            if (message.startsWith("ASSIGN")) {
                handleAssignMessage(message);
            } else if (message.startsWith("START")) {
                statusLabel.setText("Game started.");
            } else if (message.startsWith("TURN")) {
                handleTurnMessage(message);
            } else if (message.startsWith("VALID_MOVE")) {
                handleValidMoveMessage(message);
            } else if (message.startsWith("INVALID_MOVE")) {
                JOptionPane.showMessageDialog(this, "Invalid move.");
            } else if (message.startsWith("ERROR")) {
                JOptionPane.showMessageDialog(this, message);
            }
        });
    }

    private void handleAssignMessage(String message) {
        String[] parts = message.split(" ");

        if (parts.length == 2) {
            myStone = Stone.valueOf(parts[1]);
            setTitle("Go Network Game - You are " + myStone);
        }
    }

    private void handleTurnMessage(String message) {
        String[] parts = message.split(" ");

        if (parts.length == 2) {
            currentTurn = Stone.valueOf(parts[1]);

            if (currentTurn == myStone) {
                statusLabel.setText("Your turn: " + currentTurn);
            } else {
                statusLabel.setText("Opponent's turn: " + currentTurn);
            }
        }
    }

    private void handleValidMoveMessage(String message) {
        String[] parts = message.split(" ");

        if (parts.length == 4) {
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            Stone stone = Stone.valueOf(parts[3]);

            boardPanel.placeStone(row, col, stone);
        }
    }
}
