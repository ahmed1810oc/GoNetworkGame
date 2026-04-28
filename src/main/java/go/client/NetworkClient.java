package go.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Handles the connection between the client GUI and the server.
 */
public class NetworkClient {

    private Socket socket;
    private Scanner input;
    private PrintWriter output;

    public void connect(String serverIp, int port) throws IOException {
        socket = new Socket(serverIp, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        if (output != null) {
            output.println(message);
        }
    }

    public boolean hasNextMessage() {
        return input != null && input.hasNextLine();
    }

    public String readMessage() {
        if (input != null && input.hasNextLine()) {
            return input.nextLine();
        }

        return null;
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing socket: " + e.getMessage());
        }
    }
}