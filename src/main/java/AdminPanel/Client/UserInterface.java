package AdminPanel.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface {
    private JFrame frame;
    private JTextField serverIpField;
    private JButton connectButton;
    private ServerConn serverConn;

    public UserInterface() {
        frame = new JFrame("Connect to Server");
        frame.setSize(400, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top panel to hold IP input field and Connect button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Align items to the left

        serverIpField = new JTextField(20);  // Width of 20 columns
        serverIpField.setToolTipText("Enter Server IP Address");
        connectButton = new JButton("Connect");

        topPanel.add(new JLabel("Server IP:"));
        topPanel.add(serverIpField);
        topPanel.add(connectButton);

        frame.add(topPanel, BorderLayout.NORTH);

        // Action Listener for the Connect button
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String serverIp = serverIpField.getText();
                serverConn = new ServerConn(serverIp);
                if (serverConn.connect()) {
                    // Switch to the Main Page if connected successfully
                    frame.setVisible(false);
                    MainPageUI mainPage = new MainPageUI(serverConn);
                    mainPage.createAndShowUI();
                } else {
                    JOptionPane.showMessageDialog(frame, "Connection Failed. Try Again.");
                }
            }
        });
    }

    public void createAndShowUI() {
        frame.setVisible(true);
    }
}
