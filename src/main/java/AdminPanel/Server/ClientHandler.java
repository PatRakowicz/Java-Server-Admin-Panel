package AdminPanel.Server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private DockerController dockerController;

    // Constructor to initialize the client handler with the client socket and DockerController instance
    public ClientHandler(Socket clientSocket, DockerController dockerController) {
        this.clientSocket = clientSocket;
        this.dockerController = dockerController;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            System.out.println("Client connected: " + clientSocket.getInetAddress());
            String clientMessage;

            // Read client commands and process them
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Client command received: " + clientMessage);
                String response;

                // Handle different client commands and interact with DockerController
                if ("ListContainers".equalsIgnoreCase(clientMessage)) {
                    response = dockerController.listRunningContainers();
                } else if (clientMessage.startsWith("CreateContainer")) {
                    String[] parts = clientMessage.split(" ");
                    if (parts.length == 2) {
                        String imageName = parts[1];
                        response = dockerController.createContainer(imageName);
                    } else {
                        response = "Invalid command format. Use: CreateContainer <image_name>";
                    }
                } else {
                    response = "Unknown command";
                }

                // Send response back to the client
                out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Failed to close client socket: " + e.getMessage());
            }
        }
    }
}
