import main.java.controller.LoginController;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }

        // Start the application with login screen
        SwingUtilities.invokeLater(() -> {
            new LoginController();
        });
    }
}