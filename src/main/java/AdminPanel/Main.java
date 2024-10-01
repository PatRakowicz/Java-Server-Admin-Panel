package AdminPanel;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();

        try {
            System.out.println("Pulling hello-world image..");
            dockerClient.pullImageCmd("hello-world:latest").start().awaitCompletion();

            // Explicitly check if the image is available
            boolean imageAvailable = false;
            List<Image> images = dockerClient.listImagesCmd().exec();
            for (Image image : images) {
                if (image.getRepoTags() != null) {
                    for (String tag : image.getRepoTags()) {
                        if (tag.equals("hello-world:latest")) {
                            imageAvailable = true;
                            break;
                        }
                    }
                }
                if (imageAvailable) break;
            }

            if (imageAvailable) {
                System.out.println("Image found, creating container...");
                CreateContainerResponse container = dockerClient.createContainerCmd("hello-world").exec();
                System.out.println("Container Created with ID: " + container.getId());

                System.out.println("Starting container..");
                dockerClient.startContainerCmd(container.getId()).exec();
                System.out.println("Container started.");

                // Capture logs from the container
                System.out.println("Fetching logs from the container...");
                StringBuilder logs = new StringBuilder();
                dockerClient.logContainerCmd(container.getId())
                        .withStdOut(true)
                        .withStdErr(true)
                        .withFollowStream(true)
                        .exec(new LogContainerResultCallback() {
                            @Override
                            public void onNext(com.github.dockerjava.api.model.Frame item) {
                                logs.append(new String(item.getPayload())).append("\n");
                                System.out.print(new String(item.getPayload()));  // Print logs in real-time
                            }
                        }).awaitCompletion();

                System.out.println("Container logs captured successfully.");

                // Check if the container is still running
                boolean isRunning = false;
                for (Container c : dockerClient.listContainersCmd().withShowAll(true).exec()) {
                    if (c.getId().equals(container.getId())) {
                        isRunning = c.getState().equalsIgnoreCase("running");
                        break;
                    }
                }

                if (isRunning) {
                    System.out.println("Stopping container..");
                    dockerClient.stopContainerCmd(container.getId()).exec();
                } else {
                    System.out.println("Container is not running, skipping stop command");
                }
                System.out.println("Removing Container..");
                dockerClient.removeContainerCmd(container.getId()).exec();
                System.out.println("Container removed successfully");
            } else {
                System.out.println("Image not found, skipping container creation.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dockerClient.close();
        }
    }
}
