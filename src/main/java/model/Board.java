package main.java.model;

import java.awt.Color;

public class Board {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;
    private Color[][] grid;
    
    public Board() {
        grid = new Color[HEIGHT][WIDTH];
    }
    
    public boolean isValidMove(Tetromino tetromino, int row, int col, int rotation) {
        int[][] shape = tetromino.getShape(rotation);
        
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 1) {
                    int boardRow = row + r;
                    int boardCol = col + c;
                    
                    // Check boundaries
                    if (boardRow < 0 || boardRow >= HEIGHT || boardCol < 0 || boardCol >= WIDTH) {
                        return false;
                    }
                    
                    // Check collision with placed pieces
                    if (boardRow >= 0 && grid[boardRow][boardCol] != null) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    public void placePiece(Tetromino tetromino, int row, int col, int rotation) {
        int[][] shape = tetromino.getShape(rotation);
        
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 1) {
                    int boardRow = row + r;
                    int boardCol = col + c;
                    
                    if (boardRow >= 0 && boardRow < HEIGHT && boardCol >= 0 && boardCol < WIDTH) {
                        grid[boardRow][boardCol] = tetromino.getColor();
                    }
                }
            }
        }
    }
    
    public int clearLines() {
        int linesCleared = 0;
        
        for (int row = HEIGHT - 1; row >= 0; row--) {
            boolean isLineFull = true;
            for (int col = 0; col < WIDTH; col++) {
                if (grid[row][col] == null) {
                    isLineFull = false;
                    break;
                }
            }
            
            if (isLineFull) {
                linesCleared++;
                
                // Move all lines above this one down
                for (int r = row; r > 0; r--) {
                    System.arraycopy(grid[r-1], 0, grid[r], 0, WIDTH);
                }
                
                // Clear the top line
                for (int col = 0; col < WIDTH; col++) {
                    grid[0][col] = null;
                }
                
                row++; // Check the same row again as it now has new content
            }
        }
        
        return linesCleared;
    }
    
    public boolean isGameOver() {
        // Check if there are any blocks in the top row
        for (int col = 0; col < WIDTH; col++) {
            if (grid[0][col] != null) {
                return true;
            }
        }
        return false;
    }
    
    public Color getGridCell(int row, int col) {
        return grid[row][col];
    }
    
    public void reset() {
        grid = new Color[HEIGHT][WIDTH];
    }
}
