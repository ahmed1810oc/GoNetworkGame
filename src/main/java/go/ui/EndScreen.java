package go.ui;

import go.client.NetworkClient;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * End screen shown when the game finishes.
 */
public class EndScreen extends JFrame {

    private NetworkClient networkClient;

    public EndScreen(String winner, String blackScore, String whiteScore, NetworkClient networkClient) {
        this.networkClient = networkClient;
        setupWindow(winner, blackScore, whiteScore);
    }

    private void setupWindow(String winner, String blackScore, String whiteScore) {
        setTitle("Go Network Game - Game Over");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Game Over");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        String winnerText;

        if (winner.equals("EMPTY")) {
            winnerText = "Result: Draw";
        } else {
            winnerText = "Winner: " + winner;
        }

        JLabel winnerLabel = new JLabel(winnerText);
        JLabel blackScoreLabel = new JLabel("Black score: " + blackScore);
        JLabel whiteScoreLabel = new JLabel("White score: " + whiteScore);

        winnerLabel.setHorizontalAlignment(JLabel.CENTER);
        blackScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        whiteScoreLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel resultPanel = new JPanel(new GridLayout(3, 1));
        resultPanel.add(winnerLabel);
        resultPanel.add(blackScoreLabel);
        resultPanel.add(whiteScoreLabel);

        JButton playAgainButton = new JButton("Play Again");
        JButton exitButton = new JButton("Exit");

        playAgainButton.addActionListener(e -> {
            networkClient.sendMessage("RESTART_REQUEST");
            playAgainButton.setEnabled(false);
            playAgainButton.setText("Waiting for other player...");
        });

        exitButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(playAgainButton);
        buttonPanel.add(exitButton);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(resultPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
