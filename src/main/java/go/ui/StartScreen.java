package go.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Start screen of the game. The user enters server IP and port before
 * connecting.
 */
public class StartScreen extends JFrame {

    private JTextField serverIpField;
    private JTextField portField;
    private JTextField nameField;
    private JButton connectButton;

    public StartScreen() {
        setupWindow();
    }

    private void setupWindow() {
        setTitle("Go Network Game - Start");
        setSize(520, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Go Network Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel subtitleLabel = new JLabel("Two-player online Go game");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        serverIpField = new JTextField("localhost");
        portField = new JTextField("5000");
        nameField = new JTextField("Player");

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Connection Information"));

        formPanel.add(new JLabel("Server IP:"));
        formPanel.add(serverIpField);

        formPanel.add(new JLabel("Port:"));
        formPanel.add(portField);

        formPanel.add(new JLabel("Player Name:"));
        formPanel.add(nameField);

        JTextArea rulesArea = new JTextArea();
        rulesArea.setEditable(false);
        rulesArea.setLineWrap(true);
        rulesArea.setWrapStyleWord(true);
        rulesArea.setText(
                "How to play:\n"
                + "- Black plays first.\n"
                + "- Click an empty intersection to place a stone.\n"
                + "- Surround opponent stones to capture them.\n"
                + "- Use Pass when you do not want to move.\n"
                + "- If both players pass, the game ends.\n"
                + "- You can resign at any time."
        );
        rulesArea.setFont(new Font("Arial", Font.PLAIN, 13));
        rulesArea.setBorder(BorderFactory.createTitledBorder("Quick Rules"));

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(rulesArea, BorderLayout.CENTER);

        connectButton = new JButton("Connect to Game");
        connectButton.setFont(new Font("Arial", Font.BOLD, 16));
        connectButton.addActionListener(e -> connectToGame());

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(connectButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void connectToGame() {
        String serverIp = serverIpField.getText().trim();
        String portText = portField.getText().trim();
        String playerName = nameField.getText().trim();

        if (serverIp.isEmpty() || portText.isEmpty() || playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            int port = Integer.parseInt(portText);

            GameScreen gameScreen = new GameScreen(serverIp, port, playerName);
            gameScreen.setVisible(true);

            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Port must be a number.");
        }
    }
}
