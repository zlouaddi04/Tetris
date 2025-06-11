package main.java.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView {
    private JFrame frame;
    private JTextField usernameField;
    private JButton startButton;
    private JLabel titleLabel;
    private JLabel errorLabel;
    private JLabel welcomeLabel;
    
    public LoginView() {
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("Tetris - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Shrink size by 5%
        frame.setSize(380, 285);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Title
        titleLabel = new JLabel("Welcome to Tetris!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Username field
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel usernameLabel = new JLabel("Enter your username:");
        usernameField = new JTextField();
        inputPanel.add(usernameLabel, BorderLayout.NORTH);
        inputPanel.add(usernameField, BorderLayout.CENTER);
        
        // Error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Welcome message label for returning users
        welcomeLabel = new JLabel("");
        welcomeLabel.setForeground(new Color(0, 100, 0));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Start button
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        
        mainPanel.add(titleLabel);
        mainPanel.add(inputPanel);
        mainPanel.add(errorLabel);
        mainPanel.add(welcomeLabel);
        mainPanel.add(startButton);
        
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
    }
    
    public void setStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }
    
    public String getUsername() {
        return usernameField.getText().trim();
    }
    
    public void showError(String message) {
        errorLabel.setText(message);
        welcomeLabel.setText("");
    }
    
    public void showWelcomeMessage(String message) {
        welcomeLabel.setText(message);
        errorLabel.setText("");
        
        // Show welcome message in dialog before starting
        JOptionPane.showMessageDialog(frame, message, "Welcome", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void clearError() {
        errorLabel.setText("");
    }
    
    public void display() {
        frame.setVisible(true);
        usernameField.requestFocus();
    }
    
    public void close() {
        frame.dispose();
    }
}
