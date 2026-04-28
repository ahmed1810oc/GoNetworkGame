package go.client;

import go.ui.GameScreen;

import javax.swing.SwingUtilities;

public class ClientMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameScreen gameScreen = new GameScreen("localhost", 5000);
            gameScreen.setVisible(true);
        });
    }
}