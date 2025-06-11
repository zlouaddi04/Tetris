package main.java.model;

public class Game {
    private Board board;
    private Tetromino currentPiece;
    private Tetromino nextPiece;
    private int currentRow;
    private int currentCol;
    private int currentRotation;
    private int score;
    private boolean gameOver;
    private User currentUser;
    private UserDao userDao;
    
    public Game(User user) {
        this.currentUser = user;
        this.userDao = new UserDao();
        this.board = new Board();
        this.score = 0;
        this.gameOver = false;
        
        // Initialize with random pieces
        this.nextPiece = Tetromino.getRandomTetromino();
        spawnNewPiece();
    }
    
    public void spawnNewPiece() {
        currentPiece = nextPiece;
        nextPiece = Tetromino.getRandomTetromino();
        currentRotation = 0;
        
        // Start from top center
        currentRow = 0;
        currentCol = Board.WIDTH / 2 - 1;
        
        // Check if the game is over
        if (!board.isValidMove(currentPiece, currentRow, currentCol, currentRotation)) {
            gameOver = true;
            updateHighScore();
        }
    }
    
    public boolean moveDown() {
        if (board.isValidMove(currentPiece, currentRow + 1, currentCol, currentRotation)) {
            currentRow++;
            return true;
        } else {
            // Place the piece on the board
            board.placePiece(currentPiece, currentRow, currentCol, currentRotation);
            
            // Clear completed lines and update score
            int linesCleared = board.clearLines();
            updateScore(linesCleared);
            
            // Spawn a new piece
            spawnNewPiece();
            return false;
        }
    }
    
    public boolean moveLeft() {
        if (board.isValidMove(currentPiece, currentRow, currentCol - 1, currentRotation)) {
            currentCol--;
            return true;
        }
        return false;
    }
    
    public boolean moveRight() {
        if (board.isValidMove(currentPiece, currentRow, currentCol + 1, currentRotation)) {
            currentCol++;
            return true;
        }
        return false;
    }
    
    public boolean rotate() {
        int newRotation = (currentRotation + 1) % 4;
        if (board.isValidMove(currentPiece, currentRow, currentCol, newRotation)) {
            currentRotation = newRotation;
            return true;
        }
        return false;
    }
    
    public void hardDrop() {
        while (moveDown()) {
            // Keep moving down until it can't anymore
        }
    }
    
    private void updateScore(int linesCleared) {
        // Score based on number of lines cleared at once
        switch (linesCleared) {
            case 1:
                score += 100;
                break;
            case 2:
                score += 300;
                break;
            case 3:
                score += 500;
                break;
            case 4:
                score += 800; // Tetris!
                break;
        }
        
        // Update high score in real-time if current score exceeds previous high score
        if (score > currentUser.getHighScore()) {
            updateHighScore();
        }
    }
    
    private void updateHighScore() {
        // Only update if current score is higher than stored high score
        if (score > currentUser.getHighScore()) {
            currentUser.setHighScore(score);
            userDao.updateHighScore(currentUser);
        }
    }
    
    public Board getBoard() {
        return board;
    }
    
    public Tetromino getCurrentPiece() {
        return currentPiece;
    }
    
    public Tetromino getNextPiece() {
        return nextPiece;
    }
    
    public int getCurrentRow() {
        return currentRow;
    }
    
    public int getCurrentCol() {
        return currentCol;
    }
    
    public int getCurrentRotation() {
        return currentRotation;
    }
    
    public int getScore() {
        return score;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void restart() {
        board.reset();
        score = 0;
        gameOver = false;
        
        this.nextPiece = Tetromino.getRandomTetromino();
        spawnNewPiece();
    }
}