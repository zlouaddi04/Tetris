package main.java.controller;

import main.java.model.Game;
import main.java.model.User;
import main.java.view.GameView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {
    private Game game;
    private GameView view;
    private Timer gameTimer;
    private boolean isPaused = false;
    private static final int INITIAL_DELAY = 1000; // milliseconds
    
    public GameController(User user) {
        this.game = new Game(user);
        this.view = new GameView(game);
        
        setupKeyListeners();
        setupRestartButton();
        startGameLoop();
        
        view.display();
    }

    private void setupKeyListeners() {
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (game.isGameOver()) return;
                
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (!isPaused) game.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!isPaused) game.moveRight();
                        break;
                    case KeyEvent.VK_DOWN:
                        if (!isPaused) game.moveDown();
                        break;
                    case KeyEvent.VK_UP:
                        if (!isPaused) game.rotate();
                        break;
                    case KeyEvent.VK_SPACE:
                        if (!isPaused) game.hardDrop();
                        break;
                    case KeyEvent.VK_P:
                        togglePause();
                        break;
                }
                view.refresh();
            }
        });
    }
    
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            gameTimer.stop();
            view.showPauseOverlay();
        } else {
            gameTimer.start();
            view.hidePauseOverlay();
        }
    }
    
    private void setupRestartButton() {
        view.setRestartButtonListener(this::restartGame);
    }
    
    private void restartGame(ActionEvent e) {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        isPaused = false;
        game.restart();
        startGameLoop();
        view.refresh();
        view.hidePauseOverlay();
    }
    
    private void startGameLoop() {
        gameTimer = new Timer(INITIAL_DELAY, e -> {
            if (!game.isGameOver()) {
                game.moveDown();
                view.refresh();
                
                // Increase game speed as score increases
                int updatedDelay = INITIAL_DELAY - (game.getScore() / 1000) * 100;
                updatedDelay = Math.max(updatedDelay, 200); // Don't go below 200ms
                
                if (updatedDelay != gameTimer.getDelay()) {
                    gameTimer.setDelay(updatedDelay);
                }
            } else {
                gameTimer.stop();
                view.showGameOver();
            }
        });
        gameTimer.start();
    }
}
