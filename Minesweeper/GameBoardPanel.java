package Minesweeper;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serial;
import javax.imageio.ImageIO;
import javax.swing.*;
import static Minesweeper.MineSweeperConstants.ROWS;
import static Minesweeper.MineSweeperConstants.COLS;

public class GameBoardPanel extends JPanel {
    @Serial
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60;  // Cell width and height, in pixels
    public static final int CANVAS_WIDTH  = CELL_SIZE * COLS; // Game board width/height
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;

    // Define properties (package-visible)
    /** The game board composes of ROWSxCOLS cells */
    Cell cells[][] = new Cell[ROWS][COLS];
    /** Number of mines */
    int numMines = 10;

    /** Constructor */
    public GameBoardPanel() {
        super.setLayout(new GridLayout(ROWS, COLS, 2, 2));  // JPanel

        // Allocate the 2D array of Cell, and added into content-pane.
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);
            }
        }

        // [TODO 3] Allocate a common listener as the MouseEvent listener for all the
        //  Cells (JButtons)
        CellMouseListener listener = new CellMouseListener();

        // [TODO 4] Every cell adds this common listener
        for (Cell[] row:cells) {
            for (Cell cell : row) cell.addMouseListener(listener);
        }

        // Set the size of the content-pane and pack all the components
        //  under this container.
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }

    // Initialize and re-initialize a new game
    public void newGame() {
        // Get a new mine map
        MineMap mineMap = new MineMap();
        mineMap.newMineMap(numMines);

        // Reset cells, mines, and flags
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                // Initialize each cell with/without mine
                cells[row][col].newGame(mineMap.isMined[row][col]);
            }
        }
    }

    // Return the number of mines [0, 8] in the 8 neighboring cells
    //  of the given cell at (srcRow, srcCol).
    private int getSurroundingMines(int srcRow, int srcCol) {
        int numMines = 0;
        for (int row = srcRow - 1; row <= srcRow + 1; row++) {
            for (int col = srcCol - 1; col <= srcCol + 1; col++) {
                // Need to ensure valid row and column numbers too
                if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                    if (cells[row][col].isMined) numMines++;
                }
            }
        }
        return numMines;
    }

    // Reveal the cell at (srcRow, srcCol)
    // If this cell has 0 mines, reveal the 8 neighboring cells recursively
    private void revealCell(int srcRow, int srcCol) {
        int numMines = getSurroundingMines(srcRow, srcCol);
        cells[srcRow][srcCol].setText(numMines + "");
        cells[srcRow][srcCol].isRevealed = true;
        cells[srcRow][srcCol].paint();  // based on isRevealed
        if (numMines == 0) {
            // Recursively reveal the 8 neighboring cells
            for (int row = srcRow - 1; row <= srcRow + 1; row++) {
                for (int col = srcCol - 1; col <= srcCol + 1; col++) {
                    // Need to ensure valid row and column numbers too
                    if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                        if (!cells[row][col].isRevealed) revealCell(row, col);
                    }
                }
            }
        }
    }

    // Return true if the player has won (all cells have been revealed or were mined)
    public boolean hasWon() {
        for(Cell[] rows: cells) {
            for(Cell col: rows) {
                if(!col.isMined && !col.isRevealed) return false;
            }
        }
        return true;
    }

    // [TODO 2] Define a Listener Inner Class
    private class CellMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {         // Get the source object that fired the Event
            Cell sourceCell = (Cell)e.getSource();
            // For debugging
            System.out.println("You clicked on (" + sourceCell.row + "," + sourceCell.col + ")");
            // Left-click to reveal a cell; Right-click to plant/remove the flag.
            if (e.getButton() == MouseEvent.BUTTON1) {  // Left-button clicked
                // [TODO 5] (later, after TODO 3 and 4
                // if you hit a mine, game over
                // else reveal this cell
                if (sourceCell.isMined) {
                    sourceCell.setText("*");
                    JOptionPane.showMessageDialog(null, "Game Over!");
                } else {
                   revealCell(sourceCell.row, sourceCell.col);
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) { // right-button clicked
                // [TODO 6]
                // If this cell is flagged, remove the flag
                // else plant a flag.
                // ......
                if (!sourceCell.isFlagged) {
                    try {
                        Image img = ImageIO.read(getClass().getResource("Minesweeperflag.png"));
                        sourceCell.setIcon(new ImageIcon(img));
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }
                else {
                    try {
                        Image img = ImageIO.read(getClass().getResource("blank image.png"));
                        sourceCell.setIcon(new ImageIcon(img));
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }
            }

            // [TODO 7] Check if the player has won, after revealing this cell
            if(hasWon()) JOptionPane.showMessageDialog(null, "You won!");
        }
    }
}