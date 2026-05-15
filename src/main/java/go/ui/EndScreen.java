package go.ui;

import go.client.NetworkClient;

import javax.swing.BorderFactory;
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
        setSize(450, 340);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Game Over");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        String winnerText;

        if (winner.equals("EMPTY")) {
            winnerText = "Result: Draw";
        } else {
            winnerText = "Winner: " + winner;
        }

        JLabel winnerLabel = new JLabel(winnerText);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        winnerLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel blackScoreLabel = new JLabel("Black stones on board: " + blackScore);
        JLabel whiteScoreLabel = new JLabel("White stones on board: " + whiteScore);

        blackScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        whiteScoreLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel replayHintLabel = new JLabel("Both players must click Play Again to restart.");
        replayHintLabel.setHorizontalAlignment(JLabel.CENTER);
        replayHintLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel resultPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        resultPanel.setBorder(BorderFactory.createTitledBorder("Final Result"));
        resultPanel.add(winnerLabel);
        resultPanel.add(blackScoreLabel);
        resultPanel.add(whiteScoreLabel);
        resultPanel.add(replayHintLabel);

        JButton playAgainButton = new JButton("Play Again");
        JButton exitButton = new JButton("Exit");

        playAgainButton.setFont(new Font("Arial", Font.BOLD, 15));
        exitButton.setFont(new Font("Arial", Font.BOLD, 15));

        playAgainButton.addActionListener(e -> {
            networkClient.sendMessage("RESTART_REQUEST");
            playAgainButton.setEnabled(false);
            playAgainButton.setText("Waiting...");
        });

        exitButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 10));
        buttonPanel.add(playAgainButton);
        buttonPanel.add(exitButton);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(resultPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
