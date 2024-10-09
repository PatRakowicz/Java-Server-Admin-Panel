package AdminPanel.Server;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.List;

public class DockerController {
    private DockerClient dockerClient;

    // Constructor to initialize the Docker client
    public DockerController() {
        initializeDockerClient();
    }

    // Initialize the Docker client with default configuration
    private void initializeDockerClient() {
        // Check environment variable or system property for Docker host
        String dockerHost = System.getenv("DOCKER_HOST");

        // Fallback to default Unix socket if no environment variable is found
        if (dockerHost == null || dockerHost.isEmpty()) {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                dockerHost = "tcp://localhost:2375";  // Use the TCP configuration if enabled
            } else {
                dockerHost = "unix:///var/run/docker.sock"; // Default Unix socket for Linux/macOS
            }
        }

        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerHost)
                .build();
        dockerClient = DockerClientBuilder.getInstance(config).build();

        System.out.println("Docker client initialized with host: " + dockerHost);
    }

    // Method to list currently running Docker containers
    public String listRunningContainers() {
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
        return containers.toString().trim();
    }

    // Method to create a new Docker container from an image name
    public String createContainer(String imageName) {
        try {
            CreateContainerResponse container = dockerClient.createContainerCmd(imageName).exec();
            return "Container created with ID: " + container.getId();
        } catch (Exception e) {
            return "Error creating container: " + e.getMessage();
        }
    }

    // Other Docker-related methods can be added here (e.g., stopContainer, getLogs, etc.)
}
