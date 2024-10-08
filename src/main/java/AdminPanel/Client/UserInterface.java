package AdminPanel.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface {
    private JFrame frame;
    private JTextField serverIpField;
    private JButton connectButton;
    private JTextArea statusArea;
    private ServerConn serverConn;

    public UserInterface() {
        frame = new JFrame("Admin Panel");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top panel to hold IP input field and Connect button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        serverIpField = new JTextField(20);
        serverIpField.setToolTipText("Enter Server IP");

        connectButton = new JButton("Connect");

        // Adding IP field and button to the top panel
        topPanel.add(new JLabel("Server IP:"));
        topPanel.add(serverIpField);
        topPanel.add(connectButton);

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);

        // Adding components to the frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(statusArea), BorderLayout.CENTER);

        // Action Listener for button
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String serverIp = serverIpField.getText();
                serverConn = new ServerConn(serverIp, statusArea);
                serverConn.connect();
            }
        });
    }

    public void createAndShowUI() {
        frame.setVisible(true);
    }
}
