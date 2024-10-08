package AdminPanel.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewContainerUI {
    private JFrame frame;
    private ServerConn serverConn;
    private JTextField imageField;
    private JButton createButton;

    public NewContainerUI(ServerConn serverConn) {
        this.serverConn = serverConn;
        frame = new JFrame("Create New Docker Container");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close only this window
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        imageField = new JTextField(20);
        imageField.setToolTipText("Enter Docker Image (e.g., nginx:latest)");

        createButton = new JButton("Create");

        inputPanel.add(new JLabel("Docker Image:"));
        inputPanel.add(imageField);
        inputPanel.add(createButton);

        frame.add(inputPanel, BorderLayout.CENTER);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String imageName = imageField.getText();
                if (serverConn.createContainer(imageName)) {
                    JOptionPane.showMessageDialog(frame, "Container Created Successfully!");
                    frame.setVisible(false);  // Hide the current window
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to Create Container.");
                }
            }
        });
    }

    public void createAndShowUI() {
        frame.setVisible(true);
    }
}