package AdminPanel.Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerConn {
    private static final int SERVER_PORT = 9090;  // Hardcoded server port
    private final String serverIp;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ServerConn(String serverIp) {
        this.serverIp = serverIp;
    }

    public boolean connect() {
        try {
            socket = new Socket(serverIp, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String sendCommand(String command) {
        try {
            out.println(command);
            return in.readLine();  // Read the server's response
        } catch (Exception e) {
            return "Failed to send command: " + e.getMessage();
        }
    }

    public List<String> listContainers() {
        String response = sendCommand("ListContainers");
        List<String> containers = new ArrayList<>();

        // If the server returns "No running containers", we add this as a single entry
        if ("No running containers".equals(response)) {
            containers.add(response);
        } else if (response != null && !response.trim().isEmpty()) {
            // Split the response into individual containers if valid
            for (String line : response.split("\n")) {
                containers.add(line.trim());
            }
        }
        return containers;
    }

    public boolean createContainer(String imageName) {
        String response = sendCommand("CreateContainer " + imageName);
        return response != null && response.startsWith("Container created");
    }

    public String getContainerLogs(String containerId) {
        return sendCommand("GetLogs " + containerId);
    }
}