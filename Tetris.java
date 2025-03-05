// Q.N. 3(b)
// A Game of Tetris 
// Functionality: 
// Queue: Use a queue to store the sequence of falling blocks. 
// Stack: Use a stack to represent the current state of the game board. 
// GUI: 
// A game board with grid cells. 
// A preview area to show the next block. 
// Buttons for left, right, and rotate. 
// Implementation: 
// Initialization: 
// Create an empty queue to store the sequence of falling blocks. 
// Create an empty stack to represent the game board. 
// Initialize the game board with empty cells. 
// Generate a random block and enqueue it. 
// Game Loop: 
// While the game is not over: 
// Check for game over: If the top row of the game board is filled, the game is over. 
// Display the game state: Draw the current state of the game board and the next block in the 
// preview area. 
// Handle user input: 
// If the left or right button is clicked, move the current block horizontally if possible. 
// If the rotate button is clicked, rotate the current block if possible. 
// Move the block: If the current block can move down without colliding, move it down. Otherwise: 
// Push the current block onto the stack, representing its placement on the game board. 
// Check for completed rows: If a row is filled, pop it from the stack and add a new empty row at the top. 
// Generate a new random block and enqueue it. 
// Game Over: 
// Display a game over message and the final score. 
// Data Structures: 
// Block: A class or struct to represent a Tetris block, including its shape, color, and current position. 
// GameBoard: A 2D array or matrix to represent the game board, where each cell can be empty or filled with a block. 
// Queue: A queue to store the sequence of falling blocks. 
// Stack: A stack to represent the current state of the game board. 
// Additional Considerations: 
// Collision detection: Implement a function to check if a block can move or rotate without colliding with other blocks or the game board boundaries. 
// Scoring: Implement a scoring system based on factors like completed rows, number of blocks placed, and other game-specific rules. 
// Leveling: Increase the speed of the falling blocks as the player's score increases. 
// Power-ups: Add power-ups like clearing lines, adding extra rows, or changing the shape of the current block. 

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javax.swing.*;

public class Tetris extends JPanel implements ActionListener {
    private static final int ROWS = 20;          // Number of rows in the game board
    private static final int COLS = 10;          // Number of columns in the game board
    private static final int BLOCK_SIZE = 30;    // Size of each block
    private static final int TIMER_DELAY = 500;  // Delay between block drops (in milliseconds)

    private Timer timer;  // Timer to control the game loop
    private int[][] board = new int[ROWS][COLS]; // 2D array representing the game board
    private Queue<int[][]> blockQueue = new LinkedList<>(); // Queue to store falling blocks
    private int[][] currentBlock;  // 2D array representing the current falling block
    private int blockRow, blockCol; // Position of the current block
    private int score = 0; // Player's score
    private boolean gameOver = false;  // Flag to check if the game is over
    private boolean showGameOverScreen = false; // Flag to show the game over screen

    // Constructor to initialize the game
    public Tetris() {
        setPreferredSize(new Dimension(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TetrisKeyAdapter());
        generateNewBlock();  // Generate the first block
        timer = new Timer(TIMER_DELAY, this); // Initialize the game timer
        timer.start();  // Start the game loop
    }

    // Generates a new block and enqueues it
    private void generateNewBlock() {
        int[][][] possibleBlocks = {
            {{1, 1, 1, 1}}, // I Block
            {{1, 1}, {1, 1}}, // O Block
            {{1, 1, 0}, {0, 1, 1}}, // S Block
            {{0, 1, 1}, {1, 1, 0}}, // Z Block
            {{1, 1, 1}, {0, 1, 0}}, // T Block
        };
        Random random = new Random();
        if (blockQueue.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                blockQueue.add(possibleBlocks[random.nextInt(possibleBlocks.length)]);
            }
        }
        currentBlock = blockQueue.poll();  // Get the next block
        blockQueue.add(possibleBlocks[random.nextInt(possibleBlocks.length)]);  // Add a new block to the queue
        blockRow = 0;  // Start from the top row
        blockCol = COLS / 2 - currentBlock[0].length / 2;  // Start from the middle column
        if (!isValidMove(blockRow, blockCol)) {  // If the block can't fit, the game is over
            gameOver = true;
            showGameOverScreen = true;
        }
    }

    // Checks if a move is valid (collision detection)
    private boolean isValidMove(int newRow, int newCol) {
        return isValidMove(newRow, newCol, currentBlock);
    }

    // Overloaded method to handle rotated blocks
    private boolean isValidMove(int newRow, int newCol, int[][] block) {
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[i].length; j++) {
                if (block[i][j] == 1) {
                    int r = newRow + i, c = newCol + j;
                    if (r >= ROWS || c < 0 || c >= COLS || (r >= 0 && board[r][c] == 1)) {
                        return false;  // Collision detected
                    }
                }
            }
        }
        return true;  // No collision, valid move
    }

    // Places the block in the stack (game board state)
    private void placeBlock() {
        for (int i = 0; i < currentBlock.length; i++) {
            for (int j = 0; j < currentBlock[i].length; j++) {
                if (currentBlock[i][j] == 1) {
                    int row = blockRow + i;
                    int col = blockCol + j;
                    if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                        board[row][col] = 1; // Place the block on the board
                    } else {
                        System.out.println("Out of bounds: row=" + row + ", col=" + col);
                        gameOver = true;
                        showGameOverScreen = true;
                        return;
                    }
                }
            }
        }
        clearRows();  // Check for completed rows
        generateNewBlock();  // Generate a new block
    }

    // Clears completed rows and updates the score
    private void clearRows() {
        for (int i = 0; i < ROWS; i++) {
            boolean fullRow = true;
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == 0) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                score += 100;  // Increase score for clearing a row
                for (int k = i; k > 0; k--) {
                    board[k] = board[k - 1].clone();
                }
                board[0] = new int[COLS];  // Add a new empty row at the top
            }
        }
    }

    // Moves the block left or right
    private void moveBlock(int dx) {
        if (isValidMove(blockRow, blockCol + dx)) {
            blockCol += dx;
        }
    }

    // Moves the block down automatically
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (isValidMove(blockRow + 1, blockCol)) {
                blockRow++;  // Move block down
            } else {
                placeBlock();  // Place the block on the board
            }
            repaint();  // Redraw the game state
        } else {
            timer.stop(); // Stop the timer when the game is over
        }
    }

    // Draw the game components on the panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the game board
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == 1) {
                    g.setColor(Color.BLUE);  // Block color
                    g.fillRect(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        // Draw the current falling block
        g.setColor(Color.RED);  // Falling block color
        for (int i = 0; i < currentBlock.length; i++) {
            for (int j = 0; j < currentBlock[i].length; j++) {
                if (currentBlock[i][j] == 1) {
                    g.fillRect((blockCol + j) * BLOCK_SIZE, (blockRow + i) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        // If game over, display a message and final score
        if (showGameOverScreen) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("GAME OVER!", 80, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Final Score: " + score, 100, 200);
        }
    }

    // Handles user input for controlling the game
    private class TetrisKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (gameOver) return;
            if (e.getKeyCode() == KeyEvent.VK_LEFT) moveBlock(-1);  // Move left
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveBlock(1);   // Move right
            if (e.getKeyCode() == KeyEvent.VK_DOWN) actionPerformed(null);  // Move down
            if (e.getKeyCode() == KeyEvent.VK_SPACE) rotateBlock();  // Rotate block
            repaint();  // Redraw the game
        }
    }

    // Rotates the current block if possible
    private void rotateBlock() {
        int[][] rotatedBlock = new int[currentBlock[0].length][currentBlock.length];
        for (int i = 0; i < currentBlock.length; i++) {
            for (int j = 0; j < currentBlock[i].length; j++) {
                rotatedBlock[j][currentBlock.length - 1 - i] = currentBlock[i][j];
            }
        }
        if (isValidMove(blockRow, blockCol, rotatedBlock)) {
            currentBlock = rotatedBlock;  // Apply the rotation
        }
    }

    // Main method to start the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        Tetris game = new Tetris();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

/* Algorithm Explanation:

 1. Initialization:
   - The game board is represented by a 2D array `board`.
   - A queue `blockQueue` stores upcoming blocks.
   - The `currentBlock` holds the block currently falling.
   - The `timer` controls the game's pace.

 2. Game Loop (controlled by the `Timer`):
   - `actionPerformed` method is called periodically (every `TIMER_DELAY`).
   - It checks for game over (`gameOver`).
   - If not game over, it attempts to move the `currentBlock` down.
   - Collision Detection (`isValidMove`): Before moving, it checks if the new position is valid:
     - It iterates through the block's cells.
     - It checks if the new coordinates are within the board boundaries.
     - It checks if the new position collides with any existing blocks on the board.
   - If the move down is valid, `blockRow` is incremented.
   - If the move down is invalid (collision or bottom reached):
     - The block is placed on the board (`placeBlock`).
       - The block's cells are copied to the `board` array.
     - Completed rows are cleared (`clearRows`).
       - Iterates through each row of the `board`.
       - If a row is full (all cells are 1), it's cleared.
       - Rows above the cleared row are shifted down.
     - A new block is generated (`generateNewBlock`) and added to the queue.
   - The game board is redrawn (`repaint`).

 3. User Input (handled by `TetrisKeyAdapter`):
   - Key presses (LEFT, RIGHT, DOWN, SPACE) are detected.
   - LEFT/RIGHT: Attempts to move the block horizontally (`moveBlock`), checking for validity first.
   - DOWN: Forces the block to move down by calling `actionPerformed`.
   - SPACE: Rotates the block (`rotateBlock`), checking for validity first. Rotation is done by creating new matrix.
   - The game board is redrawn (`repaint`).

 4. Game Over:
   - If a new block cannot be placed on the board during `generateNewBlock` (because the top is blocked), `gameOver` is set to true.
   - The `timer` is stopped.
   - A "Game Over" message and the final score are displayed.

 5. Data Structures:
   - `board`: 2D array representing the game board.  Acts like a stack implicitly - the placed blocks form the filled portion.
   - `blockQueue`: Queue used to hold the next blocks that will fall.
*/