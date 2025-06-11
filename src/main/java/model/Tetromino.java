package main.java.model;

import java.awt.Color;
import java.util.Random;

public class Tetromino {
    // Shape types
    public static final int I = 0;
    public static final int J = 1;
    public static final int L = 2;
    public static final int O = 3;
    public static final int S = 4;
    public static final int T = 5;
    public static final int Z = 6;
    
    private static final Color[] COLORS = {
        Color.CYAN,    // I
        Color.BLUE,    // J
        Color.ORANGE,  // L
        Color.YELLOW,  // O
        Color.GREEN,   // S
        Color.MAGENTA, // T
        Color.RED      // Z
    };
    
    // Tetromino shapes defined in a 4x4 grid with rotations
    private static final int[][][][] SHAPES = {
        // I shape
        {
            {{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}},
            {{0, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}},
            {{0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}}
        },
        // J shape
        {
            {{1, 0, 0, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 1, 1, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}},
            {{0, 0, 0, 0}, {1, 1, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 0}},
            {{0, 1, 0, 0}, {0, 1, 0, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}}
        },
        // L shape
        {
            {{0, 0, 1, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}},
            {{0, 0, 0, 0}, {1, 1, 1, 0}, {1, 0, 0, 0}, {0, 0, 0, 0}},
            {{1, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}}
        },
        // O shape
        {
            {{0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}}
        },
        // S shape
        {
            {{0, 1, 1, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 0}},
            {{0, 0, 0, 0}, {0, 1, 1, 0}, {1, 1, 0, 0}, {0, 0, 0, 0}},
            {{1, 0, 0, 0}, {1, 1, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}}
        },
        // T shape
        {
            {{0, 1, 0, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 1, 0, 0}, {0, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}},
            {{0, 0, 0, 0}, {1, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}},
            {{0, 1, 0, 0}, {1, 1, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}}
        },
        // Z shape
        {
            {{1, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 0, 1, 0}, {0, 1, 1, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}},
            {{0, 0, 0, 0}, {1, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}},
            {{0, 1, 0, 0}, {1, 1, 0, 0}, {1, 0, 0, 0}, {0, 0, 0, 0}}
        }
    };
    
    private int type;
    private Color color;
    
    public Tetromino(int type) {
        this.type = type;
        this.color = COLORS[type];
    }
    
    public static Tetromino getRandomTetromino() {
        Random random = new Random();
        return new Tetromino(random.nextInt(7));
    }
    
    public int[][] getShape(int rotation) {
        // Extract actual shape from definition
        int[][] fullShape = SHAPES[type][rotation % 4];
        
        // Count dimensions to create a smaller shape
        int minRow = 4, maxRow = -1, minCol = 4, maxCol = -1;
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (fullShape[r][c] == 1) {
                    minRow = Math.min(minRow, r);
                    maxRow = Math.max(maxRow, r);
                    minCol = Math.min(minCol, c);
                    maxCol = Math.max(maxCol, c);
                }
            }
        }
        
        // If no cells found (shouldn't happen)
        if (maxRow < 0) return new int[0][0];
        
        // Create minimal shape
        int rows = maxRow - minRow + 1;
        int cols = maxCol - minCol + 1;
        int[][] shape = new int[rows][cols];
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                shape[r][c] = fullShape[r + minRow][c + minCol];
            }
        }
        
        return shape;
    }
    
    public Color getColor() {
        return color;
    }
    
    public int getType() {
        return type;
    }
}
