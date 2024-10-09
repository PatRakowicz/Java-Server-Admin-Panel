package AdminPanel.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int SERVER_PORT = 9090;
    private DockerController dockerController;

    public Server() {
        // Initialize DockerController
        dockerController = new DockerController();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    // Start the server and listen for client connections
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started. Waiting for connections...");

            while (true) {
                // Accept a new client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // Create a new ClientHandler instance for each new client and run it in a separate thread
                ClientHandler clientHandler = new ClientHandler(clientSocket, dockerController);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();  // Start the client handler thread
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
