package AdminPanel.Client;

import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
           UserInterface ui = new UserInterface();
            ui.createAndShowUI();
        });
    }
}
