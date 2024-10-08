package AdminPanel.Server;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private static final int SERVER_PORT = 9090;
    private static DockerClient dockerClient;

    public static void main(String[] args) {
        // Initialize DockerClient using default configuration
        initializeDockerClient();

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started. Waiting for connection...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    String clientMessage = in.readLine();
                    System.out.println("Client command received: " + clientMessage);

                    // Handle client commands using Docker API
                    if ("ListContainers".equalsIgnoreCase(clientMessage)) {
                        String containerList = listRunningContainers();
                        out.println(containerList);
                    } else if (clientMessage.startsWith("CreateContainer")) {
                        String[] parts = clientMessage.split(" ");
                        if (parts.length == 2) {
                            String imageName = parts[1];
                            String response = createContainer(imageName);
                            out.println(response);
                        } else {
                            out.println("Invalid command format. Use: CreateContainer <image_name>");
                        }
                    } else {
                        out.println("Unknown command");
                    }
                } catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    // Initialize DockerClient
    private static void initializeDockerClient() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock")  // Path to Docker socket
                .build();
        dockerClient = DockerClientBuilder.getInstance(config).build();
    }

    // Method to list currently running Docker containers using Docker Java API
    private static String listRunningContainers() {
        StringBuilder containers = new StringBuilder();
        try {
            List<Container> containerList = dockerClient.listContainersCmd().exec();
            if (containerList.isEmpty()) {
                return "No running containers";
            }
            for (Container container : containerList) {
                containers.append(container.getId()).append(": ").append(container.getImage()).append("\n");
            }
        } catch (Exception e) {
            return "Error listing containers: " + e.getMessage();
        }
        return containers.toString().trim();  // Remove any trailing newline
    }

    // Method to create a new Docker container using Docker Java API
    private static String createContainer(String imageName) {
        try {
            CreateContainerResponse container = dockerClient.createContainerCmd(imageName).exec();
            return "Container created with ID: " + container.getId();
        } catch (Exception e) {
            return "Error creating container: " + e.getMessage();
        }
    }
}
