/* 
 * Name: Justin Kim
 * Login: cs8bwadr
 * Date: 1/28/16
 * File: Board.java 
 * Sources of Help: None 
 * 
 * Creates 2048 board. Lets player save and load files, as well as rotate
 * the board and move the tiles.
 */
//------------------------------------------------------------------//
// Board.java                                                       //
//                                                                  //
// Class used to represent a 2048 game board                        //
//                                                                  //
// Author:  W16-CSE8B-TA group                                      //
// Date:    1/17/16                                                 //
//------------------------------------------------------------------//

/**
 * Sample Board
 * <p/>
 * 0   1   2   3
 * 0   -   -   -   -
 * 1   -   -   -   -
 * 2   -   -   -   -
 * 3   -   -   -   -
 * <p/>
 * The sample board shows the index values for the columns and rows
 * Remember that you access a 2D array by first specifying the row
 * and then the column: grid[row][column]
 */

import java.util.*;
import java.io.*;

public class Board {
    public final int NUM_START_TILES = 2;
    public final int TWO_PROBABILITY = 90;
    public final int GRID_SIZE;

    private final Random random;
    private int[][] grid;
    private int score;

    // TODO PSA3
    // Constructs a fresh board with random tiles
    public Board(int boardSize, Random random) {
	if (boardSize < 0) 
	    boardSize = 4;

	GRID_SIZE = boardSize;

	this.random = random;

	grid = new int[GRID_SIZE][GRID_SIZE];

	score = 0;

	for (int i = 0; i < NUM_START_TILES; i++) 
	    this.addRandomTile();
    }

    // TODO PSA3
    // Construct a board based off of an input file
    public Board(String inputBoard, Random random) throws IOException {
	this.random = random;
	int gridSize = 0;

	Scanner input = new Scanner(new File(inputBoard));

	while (input.hasNextInt()) {
	    gridSize = input.nextInt();

	    grid = new int[gridSize][gridSize];

	    score = input.nextInt();

	    for (int x = 0; x < gridSize; x++) {
		for (int y = 0; y < gridSize; y++)
		    grid[x][y] = input.nextInt();
	    }

	}
	GRID_SIZE = gridSize;
    }

    // TODO PSA3
    // Saves the current board to a file
    public void saveBoard(String outputBoard) throws IOException {
	/* 
	 * Name: saveBoard
	 * Purpose: Saves current board to a new file for later use.
	 * Parameters: Strong outputBoard; name of file to save to.
	 * Return: Void.
	 *
	 */

	File file = new File(outputBoard);
	PrintWriter outPut = new PrintWriter(file);

	outPut.println(GRID_SIZE);
	outPut.println(score);

	for (int x = 0; x < GRID_SIZE; x++) {
	    for (int y = 0; y < GRID_SIZE; y++) {
		outPut.print(grid[x][y] + " ");
	    }
	    outPut.println();
	}

	outPut.close();
    }

    // TODO PSA3
    // Adds a random tile (of value 2 or 4) to a
    // random empty space on the board
    public void addRandomTile() {
	/* 
	 * Name: addRandomTile
	 * Purpose: Places tile of 2 or 4 on board based on randomly 
	 * generated
	 * location.
	 * Parameters: None.
	 * Return: Void.
	 *
	 */

	int count = 0;
	int currentLocation = 0;

	for (int x = 0; x < GRID_SIZE; x++) {
	    for (int y = 0; y < GRID_SIZE; y++)
		if (grid[x][y] == 0) {
		    count++;
		}
	}

	if (count == 0)
	    return;

	Random generator = this.random;
	int location = generator.nextInt(count);
	int value = generator.nextInt(100);

	for (int x = 0; x < GRID_SIZE; x++) {
	    for (int y = 0; y < GRID_SIZE; y++) {
		if (grid[x][y] == 0) {
		    if (location == currentLocation) {
			if (value < TWO_PROBABILITY)
			    grid[x][y] = 2;

			else grid[x][y] = 4;
		    }

		    currentLocation++;
		}
	    }
	}
    }

    // TODO PSA3
    // Rotates the board by 90 degrees clockwise or 90 degrees counter-clockwise.
    // If rotateClockwise == true, rotates the board clockwise , else rotates
    // the board counter-clockwise
    public void rotate(boolean rotateClockwise) {
	/* 
	 * Name: rotate
	 * Purpose: Allows rotation of board by ninety degrees clockwise or
	 * ninety degrees counter-clockwise.
	 * Parameters: boolean rotateClockwise; determines whether or not to
	 * rotate clockwise or counter-clockwise based on whether or not
	 * rotateClockwise is true (rotates clockwise) or false (rotates
	 * counter-clockwise).
	 * Return: Void.
	 *
	 */

	int[][] rotatedGrid = new int[GRID_SIZE][GRID_SIZE];
	//Rotate clockwise
	if (rotateClockwise == true) {
	    for (int x = 0; x < GRID_SIZE; x++) {
		for (int y = 0; y < GRID_SIZE; y++)
		    //Reverses rows.
		    rotatedGrid[y][GRID_SIZE - 1 - x] = grid[x][y];
	    }
	}

	//Rotate counter-clockwise.
	else {
	    for (int x = 0; x < GRID_SIZE; x++) {
		for (int y = 0; y < GRID_SIZE; y++)
		    //Reverses columns.
		    rotatedGrid[GRID_SIZE - 1 - y][x] = grid[x][y];
	    }
	}

	//Sets rotated values to original grid.
	grid = rotatedGrid;	
    }

    //Complete this method ONLY if you want to attempt at getting the extra credit
    //Returns true if the file to be read is in the correct format, else return
    //false
    public static boolean isInputFileCorrectFormat(String inputFile) {
	//The try and catch block are used to handle any exceptions
	//Do not worry about the details, just write all your conditions inside the
	//try block
	try {
	    //write your code to check for all conditions and return true if it satisfies
	    //all conditions else return false
	    return true;
	} catch (Exception e) {
	    return false;
	}
    }

    // No need to change this for PSA3
    // Performs a move Operation
    public boolean move(Direction direction) {
	/* 
	 * Name: move
	 * Purpose: Moves tile in inputted direction by calling on the
	 * helper methods for each direction, then returns true. Else the
	 * method returns false.
	 * Parameters: Direction direction- takes in enummerated values for
	 * direction and uses them for the inputted directions.
	 * Return: boolean.
	 *
	 */

	Direction up = Direction.UP;
	Direction down = Direction.DOWN;
	Direction left = Direction.LEFT;
	Direction right = Direction.RIGHT;

	ArrayList<Integer> row = new ArrayList<>(0);
	ArrayList<Integer> column = new ArrayList<>(0);

	//Checks which direction is chosen and moves the tiles.
	if (direction == up) {
	    if (canMove(up)) {
		moveHelp();
		return true;
	    }

	    else return false;
	}

	else if (direction == down) { 
	    if (canMove(down)) {
		rotate(true);
		rotate(true);
		moveHelp();
		rotate(true);
		rotate(true);
		return true;
	    }

	    else return false;
	}

	else if (direction == left) {
	    if (canMove(left)) {
		rotate(true);
		moveHelp();
		rotate(false);
		return true;
	    }

	    else return false;
	}

	else if (direction == right) {
	    if (canMove(right)) {
		rotate(false);
		moveHelp();
		rotate(true);
		return true;
	    }

	    else return false;
	}

	return false;
    }

    private void moveHelp() {
	/* 
	 * Name: moveHelp
	 * Purpose: Helper method that moves the tile up for move().
	 * Parameters: None.
	 * Return: Void.
	 *
	 */
	int newColRowElem = 0;

	//Loops through each column in grid
	for (int a = 0; a < GRID_SIZE; a++) {
	    ArrayList<Integer> colRow = new ArrayList<>(0);

	    //Loops through grid and adds non-zero integers to araylist
	    for (int y = 0; y < GRID_SIZE; y++) {
		if (grid[y][a] != 0)
		    colRow.add(grid[y][a]);
	    }

	    //Appends zeroes at end
	    for (int z = colRow.size(); z < GRID_SIZE; z++)
		colRow.add(0);

	    //Checks to see if 2 neighboring tiles match and combines them
	    for (int i = 0; i < colRow.size(); i++) {
		if ((i + 1) != colRow.size()) {
		    if (colRow.get(i).equals(colRow.get(i + 1))) {
			//Adds matching tiles together
			newColRowElem = colRow.get(i) + colRow.get(i + 1);

			//sets combined value at index and removes old values
			colRow.remove(i);
			colRow.set(i, newColRowElem);

			//Updates score
			score += (colRow.get(i));
		    }
		}

		//Appends zeroes at end
		for (int z = colRow.size(); z < GRID_SIZE; z++)
		    colRow.add(0);
	    }

	    //Resets values on grid for the current row
	    for (int y = 0; y < GRID_SIZE; y++) {
		grid[y][a] = colRow.get(y);
	    }
	}
    }

    // No need to change this for PSA3
    // Check to see if we have a game over
    public boolean isGameOver() {
	/* 
	 * Name: isGameOver
	 * Purpose: Checks to see if game is over by seeing if there are
	 * any valid moves left. If the game is not over the method will
	 * return false, else it will return true.
	 * Parameters: None.
	 * Return: boolean.
	 *
	 */

	Direction up = Direction.UP;
	Direction down = Direction.DOWN;
	Direction left = Direction.LEFT;
	Direction right = Direction.RIGHT;

	//Checks to see if any moves are available, and if not then game over.
	if (!canMove(up) && !canMove(down)) {
	    if (!canMove(left) && !canMove(right)) {
		System.out.println("Game Over!");
		return true;
	    }
	}

	return false;
    }

    // No need to change this for PSA3
    // Determine if we can move in a given direction
    public boolean canMove(Direction direction) {
	/* 
	 * Name: canMove
	 * Purpose: Checks to see if a tile can move in the inputted
	 * direction. If its neighboring tile is zero or the same number as
	 * itself, the method will return true.
	 * Parameters: Direction direction- takes in enummerated values for
	 * direction and uses them for the inputted directions.
	 * Return: boolean.
	 *
	 */

	Direction up = Direction.UP;
	Direction down = Direction.DOWN;
	Direction left = Direction.LEFT;
	Direction right = Direction.RIGHT;

	for (int x = 0; x < GRID_SIZE; x++) {
	    for (int y = 0; y < GRID_SIZE; y++) {
		if (direction.equals(up)) {
		    //Returns true if top slot is empty.
		    if (x != 0 && grid[x][y] != 0) {
			if (grid[x - 1][y] == 0)
			    return true;

			else if (grid[x - 1][y] == grid[x][y])
			    return true;
		    }
		}

		else if (direction.equals(down)) {
		    //Returns true if bottom slot is empty.
		    if (x != GRID_SIZE - 1 && grid[x][y] != 0) {
			if (grid[x + 1][y] == 0)
			    return true;

			else if (grid[x + 1][y] == grid[x][y])
			    return true;
		    }
		}

		else if (direction.equals(left)) {
		    //Returns true if left slot is empty.
		    if (y != 0 && grid[x][y] != 0) {
			if (grid[x][y - 1] == 0)
			    return true;

			else if (grid[x][y - 1] == grid[x][y])
			    return true;
		    }
		}

		else if (direction.equals(right)) {
		    //Returns true if right slot is empty.
		    if (y != GRID_SIZE - 1 && grid[x][y] != 0) {
			if (grid[x][y + 1] == 0)
			    return true;

			else if (grid[x][y + 1] == grid[x][y])
			    return true;
		    }
		}
	    }
	}
	return false;
    }

    // Return the reference to the 2048 Grid
    public int[][] getGrid() {
	return grid;
    }

    // Return the score
    public int getScore() {
	return score;
    }

    @Override
	public String toString() {
	    StringBuilder outputString = new StringBuilder();
	    outputString.append(String.format("Score: %d\n", score));
	    for (int row = 0; row < GRID_SIZE; row++) {
		for (int column = 0; column < GRID_SIZE; column++)
		    outputString.append(grid[row][column] == 0 ? "    -" :
			    String.format("%5d", grid[row][column]));

		outputString.append("\n");
	    }
	    return outputString.toString();
	}
}
