package go.ui;

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

    public EndScreen(String winner, String blackScore, String whiteScore) {
        setupWindow(winner, blackScore, whiteScore);
    }

    private void setupWindow(String winner, String blackScore, String whiteScore) {
        setTitle("Go Network Game - Game Over");
        setSize(400, 300);
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

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(resultPanel, BorderLayout.CENTER);
        mainPanel.add(exitButton, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
