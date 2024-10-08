package AdminPanel.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContainerActionsUI {
    private JFrame frame;
    private ServerConn serverConn;
    private String containerId;

    public ContainerActionsUI(ServerConn serverConn, String containerId) {
        this.serverConn = serverConn;
        this.containerId = containerId;

        frame = new JFrame("Manage Container: " + containerId);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Only close this window
        frame.setLayout(new BorderLayout());

        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new FlowLayout());

        // Buttons for Container Actions
        JButton logsButton = new JButton("View Logs");
        JButton editButton = new JButton("Edit Settings");
        JButton cliButton = new JButton("Open CLI");

        // Action Listeners for the buttons
        logsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showContainerLogs();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Edit settings not implemented yet.");
            }
        });

        cliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "CLI support coming soon!");
            }
        });

        // Adding buttons to the panel
        actionsPanel.add(logsButton);
        actionsPanel.add(editButton);
        actionsPanel.add(cliButton);

        frame.add(actionsPanel, BorderLayout.CENTER);
    }

    // Method to show container logs in a new window
    private void showContainerLogs() {
        String logs = serverConn.getContainerLogs(containerId);
        JFrame logsFrame = new JFrame("Logs for Container: " + containerId);
        logsFrame.setSize(800, 600);
        logsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Ensure only logs window closes
        JTextArea logsArea = new JTextArea(logs);
        logsArea.setEditable(false);
        logsFrame.add(new JScrollPane(logsArea));
        logsFrame.setVisible(true);
    }

    public void createAndShowUI() {
        frame.setVisible(true);
    }
}
