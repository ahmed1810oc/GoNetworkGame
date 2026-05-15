package go.ui;

import go.client.NetworkClient;
import go.model.Stone;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Main game window for the client.
 */
public class GameScreen extends JFrame {

    private NetworkClient networkClient;
    private BoardPanel boardPanel;

    private JLabel titleLabel;
    private JLabel statusLabel;
    private JLabel playerColorLabel;
    private JLabel turnLabel;
    private JLabel captureLabel;

    private JButton passButton;
    private JButton resignButton;

    private Stone myStone;
    private Stone currentTurn;
    private EndScreen endScreen;

    private String playerName;
    private String serverIp;
    private int port;

    public GameScreen(String serverIp, int port, String playerName) {
        this.serverIp = serverIp;
        this.port = port;
        this.playerName = playerName;

        networkClient = new NetworkClient();

        setupWindow();
        connectToServer(serverIp, port);
        startListeningToServer();
    }

    private void setupWindow() {
        setTitle("Go Network Game");
        setSize(720, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleLabel = new JLabel("Go Network Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        statusLabel = new JLabel("Connecting to server...");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        playerColorLabel = new JLabel("You are: Waiting...");
        playerColorLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        playerColorLabel.setHorizontalAlignment(JLabel.CENTER);

        turnLabel = new JLabel("Current turn: Waiting...");
        turnLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        turnLabel.setHorizontalAlignment(JLabel.CENTER);

        captureLabel = new JLabel("Captured - Black: 0 | White: 0");
        captureLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        captureLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(titleLabel);
        infoPanel.add(statusLabel);
        infoPanel.add(playerColorLabel);
        infoPanel.add(turnLabel);
        infoPanel.add(captureLabel);

        boardPanel = new BoardPanel();
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

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

        passButton = new JButton("Pass");
        passButton.setFont(new Font("Arial", Font.BOLD, 16));

        passButton.addActionListener(e -> {
            if (myStone == null || currentTurn == null) {
                return;
            }

            if (myStone != currentTurn) {
                JOptionPane.showMessageDialog(this, "It is not your turn.");
                return;
            }

            networkClient.sendMessage("PASS");
        });

        resignButton = new JButton("Resign");
        resignButton.setFont(new Font("Arial", Font.BOLD, 16));

        resignButton.addActionListener(e -> {
            if (myStone == null) {
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to resign?",
                    "Confirm Resign",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                networkClient.sendMessage("RESIGN");
            }
        });

        // Important: disable buttons only AFTER they are created
        passButton.setEnabled(false);
        resignButton.setEnabled(false);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 15, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 120, 15, 120));
        bottomPanel.add(passButton);
        bottomPanel.add(resignButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

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
            } else if (message.startsWith("BOARD")) {
                handleBoardMessage(message);
            } else if (message.startsWith("CAPTURES")) {
                handleCapturesMessage(message);
            } else if (message.startsWith("PASS")) {
                handlePassMessage(message);
            } else if (message.startsWith("GAME_OVER")) {
                handleGameOverMessage(message);
            } else if (message.startsWith("RESTART")) {
                handleRestartMessage();
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

            setTitle("Go Network Game - " + playerName + " - " + myStone);
            resignButton.setEnabled(true);
            playerColorLabel.setText("You are: " + myStone);
        }
    }

    private void handleTurnMessage(String message) {
        String[] parts = message.split(" ");

        if (parts.length == 2) {
            currentTurn = Stone.valueOf(parts[1]);

            turnLabel.setText("Current turn: " + currentTurn);

            if (currentTurn == myStone) {
                statusLabel.setText("Your turn. Choose a place on the board.");
                passButton.setEnabled(true);
            } else {
                statusLabel.setText("Waiting for opponent...");
                passButton.setEnabled(false);
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

    private void handlePassMessage(String message) {
        String[] parts = message.split(" ");

        if (parts.length == 2) {
            statusLabel.setText(parts[1] + " passed.");
        }
    }

    private void handleGameOverMessage(String message) {
        String[] parts = message.split(" ");

        if (parts.length == 4) {
            String winner = parts[1];
            String blackScore = parts[2];
            String whiteScore = parts[3];

            passButton.setEnabled(false);
            resignButton.setEnabled(false);

            endScreen = new EndScreen(winner, blackScore, whiteScore, networkClient);
            endScreen.setVisible(true);

            setVisible(false);
        }
    }

    private void handleRestartMessage() {
        if (endScreen != null) {
            endScreen.dispose();
            endScreen = null;
        }

        boardPanel.clearBoard();
        currentTurn = Stone.BLACK;

        captureLabel.setText("Captured - Black: 0 | White: 0");
        turnLabel.setText("Current turn: BLACK");

        resignButton.setEnabled(true);

        if (myStone == Stone.BLACK) {
            statusLabel.setText("New game started. Your turn.");
            passButton.setEnabled(true);
        } else {
            statusLabel.setText("New game started. Waiting for opponent...");
            passButton.setEnabled(false);
        }

        setVisible(true);
    }

    private void handleBoardMessage(String message) {
        String boardText = message.substring(6);

        String[] rows = boardText.split("\\|");

        if (rows.length != go.model.Board.SIZE) {
            return;
        }

        for (int row = 0; row < go.model.Board.SIZE; row++) {
            String rowText = rows[row];

            if (rowText.length() != go.model.Board.SIZE) {
                return;
            }

            for (int col = 0; col < go.model.Board.SIZE; col++) {
                char cell = rowText.charAt(col);

                if (cell == 'B') {
                    boardPanel.setStoneAt(row, col, Stone.BLACK);
                } else if (cell == 'W') {
                    boardPanel.setStoneAt(row, col, Stone.WHITE);
                } else {
                    boardPanel.setStoneAt(row, col, Stone.EMPTY);
                }
            }
        }

        boardPanel.repaint();
    }

    private void handleCapturesMessage(String message) {
        String[] parts = message.split(" ");

        if (parts.length == 3) {
            String blackCaptures = parts[1];
            String whiteCaptures = parts[2];

            captureLabel.setText("Captured - Black: " + blackCaptures + " | White: " + whiteCaptures);
        }
    }
}
