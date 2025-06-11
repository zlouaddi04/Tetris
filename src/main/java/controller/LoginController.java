package main.java.controller;

import main.java.model.User;
import main.java.model.UserDao;
import main.java.view.LoginView;

import java.awt.event.ActionEvent;

public class LoginController {
    private LoginView view;
    private UserDao userDao;
    private GameController gameController;
    
    public LoginController() {
        this.view = new LoginView();
        this.userDao = new UserDao();
        
        view.setStartButtonListener(this::handleStartGame);
        view.display();
    }
    
    private void handleStartGame(ActionEvent e) {
        String username = view.getUsername();
        
        if (username == null || username.length() < 3) {
            view.showError("Username must be at least 3 characters!");
            return;
        }
        
        view.clearError();
        
        User user = userDao.findByUsername(username);
        
        // Create new user if not found
        if (user == null) {
            user = new User(username);
            user = userDao.createUser(user);
            if (user == null) {
                view.showError("Error creating user. Please try again.");
                return;
            }
            view.showWelcomeMessage("Welcome new player: " + username);
        } else {
            // Returning user
            view.showWelcomeMessage("Welcome back, " + username + "! Your high score: " + user.getHighScore());
        }
        
        // Start the game
        view.close();
        gameController = new GameController(user);
    }
}
