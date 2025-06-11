package main.java.view;

import main.java.model.Board;
import main.java.model.Game;
import main.java.model.Tetromino;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class GameView {
    private JFrame frame;
    private BoardPanel boardPanel;
    private NextPiecePanel nextPiecePanel;
    private JLabel scoreLabel;
    private JLabel playerLabel;
    private JButton restartButton;
    private JPanel pauseOverlay;
    private Game game;
    
    public GameView(Game game) {
        this.game = game;
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(570, 665);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        
        // Main game area
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());
        // Reduced top padding to give more room for the board
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Board panel
        boardPanel = new BoardPanel();
        
        // Side panel for info and next piece
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(4, 1, 10, 10));
        sidePanel.setBackground(new Color(230, 230, 230));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Player info
        playerLabel = new JLabel("Player: " + game.getCurrentUser().getUsername());
        playerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Score panel
        JPanel scorePanel = new JPanel(new BorderLayout());
        JLabel scoreTitleLabel = new JLabel("Score:");
        scoreTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel = new JLabel("0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scorePanel.add(scoreTitleLabel, BorderLayout.NORTH);
        scorePanel.add(scoreLabel, BorderLayout.CENTER);
        
        // Next piece panel
        JPanel nextPieceContainer = new JPanel(new BorderLayout());
        JLabel nextPieceLabel = new JLabel("Next Piece:");
        nextPieceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nextPiecePanel = new NextPiecePanel();
        nextPieceContainer.add(nextPieceLabel, BorderLayout.NORTH);
        nextPieceContainer.add(nextPiecePanel, BorderLayout.CENTER);
        
        // Restart button
        restartButton = new JButton("Restart Game");
        
        // Add components to side panel
        sidePanel.add(playerLabel);
        sidePanel.add(scorePanel);
        sidePanel.add(nextPieceContainer);
        sidePanel.add(restartButton);
        
        // Add everything to main panel
        gamePanel.add(boardPanel, BorderLayout.CENTER);
        gamePanel.add(sidePanel, BorderLayout.EAST);
        
        // Controls instructions - now with more space and better layout
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.setBackground(new Color(220, 220, 220));
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Split controls into two lines for better readability
        JLabel controlsLabel = new JLabel(
            "<html><b>Controls:</b><br>" +
            "Left/Right Arrows: Move | Up Arrow: Rotate | Down Arrow: Move Down<br>" +
            "Space: Hard Drop | P: Pause Game</html>"
        );
        controlsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        controlsPanel.add(controlsLabel, BorderLayout.CENTER);
        
        // Create pause overlay (initially invisible)
        pauseOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
                
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 36));
                FontMetrics fm = g.getFontMetrics();
                String pauseText = "GAME PAUSED";
                int textWidth = fm.stringWidth(pauseText);
                g.drawString(pauseText, (getWidth() - textWidth) / 2, getHeight() / 2);
                
                g.setFont(new Font("Arial", Font.PLAIN, 18));
                fm = g.getFontMetrics();
                String resumeText = "Press 'P' to resume";
                textWidth = fm.stringWidth(resumeText);
                g.drawString(resumeText, (getWidth() - textWidth) / 2, getHeight() / 2 + 40);
            }
        };
        pauseOverlay.setOpaque(false);
        pauseOverlay.setVisible(false);
        
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(controlsPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        
        // Add pause overlay as glass pane
        frame.setGlassPane(pauseOverlay);
    }
    
    public void refresh() {
        boardPanel.repaint();
        nextPiecePanel.repaint();
        scoreLabel.setText(String.valueOf(game.getScore()));
    }
    
    public void showGameOver() {
        JOptionPane.showMessageDialog(
            frame, 
            "Game Over! Your score: " + game.getScore(), 
            "Game Over", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public void showPauseOverlay() {
        pauseOverlay.setVisible(true);
    }
    
    public void hidePauseOverlay() {
        pauseOverlay.setVisible(false);
    }
    
    public void addKeyListener(KeyListener listener) {
        frame.addKeyListener(listener);
    }
    
    public void setRestartButtonListener(java.awt.event.ActionListener listener) {
        restartButton.addActionListener(listener);
    }
    
    public void display() {
        frame.setVisible(true);
        frame.requestFocus();
    }
    
    private class BoardPanel extends JPanel {
        // Reduced cell size to ensure the entire board fits including the last row
        private static final int CELL_SIZE = 27;
        
        public BoardPanel() {
            // Added a bit of extra space to ensure everything is visible
            setPreferredSize(new Dimension(Board.WIDTH * CELL_SIZE + 2, Board.HEIGHT * CELL_SIZE + 2));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Board board = game.getBoard();
            
            // Draw the board grid and blocks
            for (int row = 0; row < Board.HEIGHT; row++) {
                for (int col = 0; col < Board.WIDTH; col++) {
                    Color color = board.getGridCell(row, col);
                    if (color != null) {
                        drawBlock(g, col, row, color);
                    }
                }
            }
            
            // Draw current piece
            if (!game.isGameOver()) {
                Tetromino current = game.getCurrentPiece();
                int[][] shape = current.getShape(game.getCurrentRotation());
                int row = game.getCurrentRow();
                int col = game.getCurrentCol();
                
                for (int r = 0; r < shape.length; r++) {
                    for (int c = 0; c < shape[r].length; c++) {
                        if (shape[r][c] == 1) {
                            drawBlock(g, col + c, row + r, current.getColor());
                        }
                    }
                }
            }
            
            // Draw grid lines
            g.setColor(Color.LIGHT_GRAY);
            for (int row = 0; row <= Board.HEIGHT; row++) {
                g.drawLine(0, row * CELL_SIZE, Board.WIDTH * CELL_SIZE, row * CELL_SIZE);
            }
            for (int col = 0; col <= Board.WIDTH; col++) {
                g.drawLine(col * CELL_SIZE, 0, col * CELL_SIZE, Board.HEIGHT * CELL_SIZE);
            }
            
            // Draw border around board to better see the edges
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, Board.WIDTH * CELL_SIZE, Board.HEIGHT * CELL_SIZE);
        }
        
        private void drawBlock(Graphics g, int col, int row, Color color) {
            int x = col * CELL_SIZE;
            int y = row * CELL_SIZE;
            
            g.setColor(color);
            g.fillRect(x + 1, y + 1, CELL_SIZE - 1, CELL_SIZE - 1);
            
            g.setColor(color.brighter());
            g.drawLine(x + 1, y + 1, x + CELL_SIZE - 1, y + 1);
            g.drawLine(x + 1, y + 1, x + 1, y + CELL_SIZE - 1);
            
            g.setColor(color.darker());
            g.drawLine(x + CELL_SIZE - 1, y + 1, x + CELL_SIZE - 1, y + CELL_SIZE - 1);
            g.drawLine(x + 1, y + CELL_SIZE - 1, x + CELL_SIZE - 1, y + CELL_SIZE - 1);
        }
    }
    
    private class NextPiecePanel extends JPanel {
        private static final int CELL_SIZE = 19;
        
        public NextPiecePanel() {
            setPreferredSize(new Dimension(6 * CELL_SIZE, 6 * CELL_SIZE));
            setBackground(new Color(230, 230, 230));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Tetromino next = game.getNextPiece();
            if (next == null) return;
            
            int[][] shape = next.getShape(0);
            int startX = (getWidth() - shape[0].length * CELL_SIZE) / 2;
            int startY = (getHeight() - shape.length * CELL_SIZE) / 2;
            
            for (int r = 0; r < shape.length; r++) {
                for (int c = 0; c < shape[r].length; c++) {
                    if (shape[r][c] == 1) {
                        int x = startX + c * CELL_SIZE;
                        int y = startY + r * CELL_SIZE;
                        
                        g.setColor(next.getColor());
                        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        
                        g.setColor(next.getColor().brighter());
                        g.drawLine(x, y, x + CELL_SIZE - 1, y);
                        g.drawLine(x, y, x, y + CELL_SIZE - 1);
                        
                        g.setColor(next.getColor().darker());
                        g.drawLine(x + CELL_SIZE - 1, y, x + CELL_SIZE - 1, y + CELL_SIZE - 1);
                        g.drawLine(x, y + CELL_SIZE - 1, x + CELL_SIZE - 1, y + CELL_SIZE - 1);
                    }
                }
            }
        }
    }
}