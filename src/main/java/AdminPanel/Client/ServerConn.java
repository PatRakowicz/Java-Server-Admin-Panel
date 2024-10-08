package AdminPanel.Client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConn {
    private static final int SERVER_PORT = 9090;
    private String serverIp;
    private JTextArea statusArea;

    public ServerConn(String serverIP, JTextArea statusArea) {
        this.serverIp = serverIP;
        this.statusArea = statusArea;
    }

    public void connect() {
        try (Socket socket = new Socket(serverIp, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println("Client Connected");

            String serverResponse = in.readLine();
            statusArea.append("Server Response: " + serverResponse + "\n");
        } catch (Exception ex) {
            statusArea.append("Connection Failed: " + ex.getMessage() + "\n");
        }
    }
}
