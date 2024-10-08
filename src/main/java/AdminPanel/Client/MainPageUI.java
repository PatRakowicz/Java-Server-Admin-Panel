package AdminPanel.Client;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPageUI {
    private JFrame frame;
    private ServerConn serverConn;

    public MainPageUI(ServerConn serverConn) {
        this.serverConn = serverConn;
        frame = new JFrame("Docker Client - Manage Containers");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Get the list of running containers
        List<String> containers = serverConn.listContainers();

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));

        // Check the first entry for "No running containers"
        if (containers.isEmpty() || (containers.size() == 1 && containers.get(0).equals("No running containers"))) {
            JLabel noContainersLabel = new JLabel("No running containers.");
            noContainersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            containerPanel.add(noContainersLabel);
        } else {
            for (String container : containers) {
                JButton containerButton = new JButton(container);
                containerButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ContainerActionsUI containerUI = new ContainerActionsUI(serverConn, container);
                        containerUI.createAndShowUI();
                    }
                });
                containerPanel.add(containerButton);
            }
        }

        JButton newButton = new JButton("New Container");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a new window for creating a container without closing the main UI
                NewContainerUI newContainerUI = new NewContainerUI(serverConn);
                newContainerUI.createAndShowUI();
            }
        });

        frame.add(containerPanel, BorderLayout.CENTER);
        frame.add(newButton, BorderLayout.SOUTH);
    }

    public void createAndShowUI() {
        frame.setVisible(true);
    }
}