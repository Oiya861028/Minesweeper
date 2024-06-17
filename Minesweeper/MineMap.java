package Minesweeper;
// "import static" constants allow us to refer to as
//   ROWS (shorthand) instead of MineSweeperConstants.ROWS
import static Minesweeper.MineSweeperConstants.ROWS;
import static Minesweeper.MineSweeperConstants.COLS;
/**
 * Define the locations of mines
 */
public class MineMap {
    // package access
    int numMines;
    boolean[][] isMined = new boolean[ROWS][COLS];
    // default is false

    // Constructor
    public MineMap() {
        super();
    }

    // Allow user to change the rows and cols
    public void newMineMap(int numMines) {
        this.numMines = numMines;
        // Hardcoded for illustration and testing, assume numMines=10
        for(int i=0;i<numMines;i++) plantMine();
    }
    private void plantMine(){
        int row = (int) (Math.random()*ROWS);
        int col = (int) (Math.random()*COLS);
        while(isMined[row][col]) {
            row = (int) (Math.random()*ROWS);
            col = (int) (Math.random()*COLS);
        }
        isMined[row][col] = true;
    }
}