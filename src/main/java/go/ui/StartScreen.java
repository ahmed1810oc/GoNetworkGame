package go.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Start screen of the game.
 * The user enters server IP and port before connecting.
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
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Go Network Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        serverIpField = new JTextField("localhost");
        portField = new JTextField("5000");
        nameField = new JTextField("Player");

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        formPanel.add(new JLabel("Server IP:"));
        formPanel.add(serverIpField);

        formPanel.add(new JLabel("Port:"));
        formPanel.add(portField);

        formPanel.add(new JLabel("Player Name:"));
        formPanel.add(nameField);

        connectButton = new JButton("Connect");

        connectButton.addActionListener(e -> connectToGame());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
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