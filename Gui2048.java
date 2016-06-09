/* 
 * Name: Justin Kim
 * Login: cs8bwadr
 * Date: 3/3/16
 * File: Gui2048.java 
 * Sources of Help: None 
 * 
 * Creates GUI for 2048. Allows user to use directional
 * keys to move up, down, left, and right, as well as 
 * saving the board to an output file in order to play
 * the game.
 *
 */

/** Gui2048.java */
/** PSA8 Release */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    private static final int TILE_WIDTH = 106;

    private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
    private static final int TEXT_SIZE_MID = 45; // Mid value tiles 
    //(128, 256, 512)
    private static final int TEXT_SIZE_HIGH = 35; // High value tiles 
    //(1024, 2048, Higher)

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
    private static final Color COLOR_2 = Color.rgb(238, 228, 218);
    private static final Color COLOR_4 = Color.rgb(237, 224, 200);
    private static final Color COLOR_8 = Color.rgb(242, 177, 121);
    private static final Color COLOR_16 = Color.rgb(245, 149, 99);
    private static final Color COLOR_32 = Color.rgb(246, 124, 95);
    private static final Color COLOR_64 = Color.rgb(246, 94, 59);
    private static final Color COLOR_128 = Color.rgb(237, 207, 114);
    private static final Color COLOR_256 = Color.rgb(237, 204, 97);
    private static final Color COLOR_512 = Color.rgb(237, 200, 80);
    private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
    private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

    private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); 
    // For tiles >= 8

    private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); 
    // For tiles < 8

    private GridPane pane;

    /** Add your own Instance Variables here */
    //Instance variables.
    private Rectangle box,boxHolder;

    private Text titleText, scoreText, text;

    private int[][] grid;

    private Rectangle[][] rectArray;
    private Text[][] textArray;

    private int tile, scoreToUpdate, scoreHolder;

    private String updatedScore;

    private final int TEXT_SIZE_GAME_OVER = 70;

    @Override
	public void start(Stage primaryStage) {
	/* 
	 * Name: start
	 * Purpose: Runs program inside a window with GUI components.
	 * Parameters: Stage primaryStage- creates window for
	 * program.
	 * Return: Void.
	 *
	 */

	    // Process Arguments and Initialize the Game Board
	    processArgs(getParameters().getRaw().toArray(new String[0]));

	    // Create the pane that will hold all of the visual objects
	    pane = new GridPane();
	    pane.setAlignment(Pos.CENTER);
	    pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
	    pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
	    // Set the spacing between the Tiles
	    pane.setHgap(15); 
	    pane.setVgap(15);

	    /** Add your Code for the GUI Here */
	    //Retrieves board.
	    grid = board.getGrid();

	    //Sets title and score.	    
	    scoreHolder = board.getScore();
	    //updatedScore = "Score: " + scoreHolder;

	    //Sets game title and score.
	    titleText = setTileText("2048", "Sans Seriff", FontWeight.BOLD, 
		    TEXT_SIZE_HIGH, Color.BLACK);
	    scoreText = setTileText(("Score: " + scoreHolder), "Sans Seriff", 
		    FontWeight.BOLD, TEXT_SIZE_HIGH, Color.BLACK);

	    //Adds on title and score.
	    pane.add(titleText, 0, 0, 2, 1);
	    GridPane.setHalignment(titleText, HPos.CENTER);
	    pane.add(scoreText, 2, 0, 2, 1);
	    GridPane.setHalignment(scoreText, HPos.CENTER);

	    //Initializes new rectangle and text arrays for board.
	    rectArray = new Rectangle[grid.length][];
	    for (int i = 0; i < grid.length; i++)
		rectArray[i] = new Rectangle[grid.length];

	    textArray = new Text[grid.length][];
	    for (int i = 0; i < grid.length; i++)
		textArray[i] = new Text[grid.length];

	    //Retrieves data from Board.java and assigns tiles to pane.
	    startTiles();
	    updateTiles();

	    //Creates scene and sets it for the stage. Also takes in direction 
	    //keys.
	    Scene scene = new Scene(pane);
	    primaryStage.setTitle("Gui2048");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    scene.setOnKeyPressed(new myKeyHandler());
	}

    //Handles keyboard movements and checks if game is over.
    private class myKeyHandler implements EventHandler<KeyEvent> {
	/* 
	 * Name: myKeyHandler
	 * Purpose: Class that handles movements through the directional
	 * keys through implementing the EventHandler interface. Also
	 * checks if game is over.
	 * Parameters: None.
	 * Return: None.
	 *
	 */

	@Override
	    public void handle(KeyEvent event) {
		/* 
		 * Name: setTileBox
		 * Purpose: Moves tiles in specified direction according
		 * to input and checks if game is over.
		 * Parameters: KeyEvent event- reads in key from keyboard.
		 * Return: Void.
		 *
		 */

		if (event.getCode() == KeyCode.UP) {
		    //Checks if tiles can move and moves them accordingly
		    //as well as adding a tile randomly and
		    //updating the tiles.
		    if (board.canMove(Direction.UP)) {
			board.move(Direction.UP);
			board.addRandomTile();
			updateTiles();
			System.out.println("Moving UP");

			//Checks to see if game is over after each move.
			if (board.isGameOver()) {
			    gameOver();
			    System.out.println("Game Over!");
			}
		    }
		}

		else if (event.getCode() == KeyCode.DOWN) {
		    //Checks if tiles can move and moves them accordingly
		    //as well as adding a tile randomly and
		    //updating the tiles.
		    if (board.canMove(Direction.DOWN)) {
			board.move(Direction.DOWN);
			board.addRandomTile();
			updateTiles();
			System.out.println("Moving DOWN");

			//Checks to see if game is over after each move.
			if (board.isGameOver()) {
			    gameOver();
			    System.out.println("Game Over!");
			}
		    }
		}

		else if (event.getCode() == KeyCode.LEFT) {
		    //Checks if tiles can move and moves them accordingly
		    //as well as adding a tile randomly and
		    //updating the tiles.
		    if (board.canMove(Direction.LEFT)) {
			board.move(Direction.LEFT);
			board.addRandomTile();
			updateTiles();
			System.out.println("Moving LEFT");

			//Checks to see if game is over after each move.
			if (board.isGameOver()) {
			    gameOver();
			    System.out.println("Game Over!");
			}
		    }
		}

		else if (event.getCode() == KeyCode.RIGHT) {
		    //Checks if tiles can move and moves them accordingly
		    //as well as adding a tile randomly and
		    //updating the tiles.
		    if (board.canMove(Direction.RIGHT)) {
			board.move(Direction.RIGHT);
			board.addRandomTile();
			updateTiles();
			System.out.println("Moving RIGHT");

			//Checks to see if game is over after each move.
			if (board.isGameOver()) {
			    gameOver();
			    System.out.println("Game Over!");
			}
		    }
		}

		//Checks if the "s" key is pressed and proceeds to
		//save the game to an output file if pressed.
		//Catches any errors when saving.
		else if (event.getCode() == KeyCode.S) {
		    try {
			board.saveBoard(outputBoard);
		    } catch (IOException exception) { 
			System.out.println("saveBoard threw an Exception");
		    }

		    System.out.println("Saving Board to " + outputBoard);
		}

		//Checks to see if game is over.
		else if (board.isGameOver()) {
		    gameOver();
		    System.out.println("Game Over!");
		}
	    }
    }

    /** Add your own Instance Methods Here */
    private Rectangle setTileBox(int width, int height, Color fill) {
	/* 
	 * Name: setTileBox
	 * Purpose: Setter method that sets tile's width, height, and
	 * color.
	 * Parameters: int width, height- rectangle dimensions,
	 * Color fill- color of rectangle.
	 * Return: Rectangle.
	 *
	 */

	//Creates new Rectangle object and sets dimensions and box color.
	Rectangle rect = new Rectangle();
	rect.setWidth(width);
	rect.setHeight(height);
	rect.setFill(fill);

	return rect;
    }

    private Text setTileText(String tileName, String fontName, FontWeight 
	    strokeWeight, int fontSize, Color fontColor) {
	/* 
	 * Name: setTileText
	 * Purpose: Setter method that sets tile text's value name, font,
	 * stroke weight, font size, and font color.
	 * Parameters: String tileName, fontName- names of the value for
	 * the tile and its font; FontWeight strokeWeight- how thick the
	 * text will be; int fontSize- how big the font will be, Color
	 * fontColor- color of the text.
	 * Return: Text.
	 *
	 */

	//Creates new Text object and sets font style, weight, and size, as 
	//well as color.
	Text text = new Text();
	text.setText(tileName);
	text.setFont(Font.font(fontName, strokeWeight, fontSize));
	text.setFill(fontColor);

	return text;
    }

    private void gameOver() {
	/* 
	 * Name: gameOver
	 * Purpose: Method that creates a "Game Over!" message
	 * using new rectangle and text objects.
	 * Parameters: None.
	 * Return: Void.
	 *
	 */

	//Creates rectangle and text for Game Over message.
	Rectangle gameOverBox = new Rectangle();
	Text gameOverText = new Text();

	//Sets properties of message's rectangle and text.
	gameOverBox.setFill(COLOR_GAME_OVER);
	gameOverBox.setWidth(pane.getWidth());
	gameOverBox.setHeight(pane.getHeight());
	gameOverText.setText("Game Over!");
	gameOverText.setFont((Font.font("Comic Sans", FontWeight.BOLD, 
			TEXT_SIZE_GAME_OVER)));
	gameOverText.setFill(COLOR_VALUE_DARK);

	//Adds on rectangle and text and centers them.
	pane.add(gameOverBox, 0, 0, grid.length, grid.length + 1);
	pane.add(gameOverText, 0, 2, grid.length, (grid.length / 2));
	GridPane.setHalignment(gameOverBox, HPos.CENTER);
	GridPane.setValignment(gameOverBox, VPos.CENTER);
	GridPane.setHalignment(gameOverText, HPos.CENTER);
	GridPane.setValignment(gameOverText, VPos.CENTER);
    }

    //Initializes board with empty tiles.
    private void startTiles() {
	/* 
	 * Name: startTiles()
	 * Purpose: Method that intializes the board with empty
	 * tiles.
	 * Parameters: None.
	 * Return: None.
	 *
	 */

	grid = board.getGrid();
	for (int x = 0; x < grid.length; x++) {
	    for (int y = 0; y < grid.length; y++) {
		box = rectArray[x][y];
		text = textArray[x][y];

		//Sets tile's properties.
		box = setTileBox(TILE_WIDTH,TILE_WIDTH,COLOR_EMPTY);
		text = setTileText("", "Comic Sans", FontWeight.BOLD, 
			TEXT_SIZE_HIGH, COLOR_VALUE_LIGHT);

		//Adds on empty tiles.
		pane.add(box, y, (x + 1)); 
		pane.add(text, y, (x + 1)); 
		GridPane.setHalignment(text, HPos.CENTER);

		//Returns changed aspects of element to index.
		rectArray[x][y] = box;
		textArray[x][y] = text;
	    }
	}
    }

    //Updates the board and score.
    private void updateTiles() {
	/* 
	 * Name: updateTiles
	 * Purpose: Method that updates the board.
	 * Parameters: None.
	 * Return: None.
	 *
	 */

	grid = board.getGrid();
	for (int x = 0; x < grid.length; x++) {
	    for (int y = 0; y < grid.length; y++) {
		tile = grid[x][y];
		box = rectArray[x][y];
		text = textArray[x][y];

		//Changes tiles' texts and colors depending
		//on what the tile's value is.
		if (tile == 0) {
		    box.setFill(COLOR_EMPTY);
		    text.setText("");
		}

		else if (tile == 2) {
		    box.setFill(COLOR_2);
		    text.setText("2");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_LOW));
		    text.setFill(COLOR_VALUE_DARK);
		}

		else if (tile == 4) {
		    box.setFill(COLOR_4);
		    text.setText("4");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_LOW));
		    text.setFill(COLOR_VALUE_DARK);
		}

		else if (tile == 8) {
		    box.setFill(COLOR_8);
		    text.setText("8");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_LOW));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		else if (tile == 16) {
		    box.setFill(COLOR_16);
		    text.setText("16");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_LOW));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		else if (tile == 32) {
		    box.setFill(COLOR_32);
		    text.setText("32");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_LOW));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		else if (tile == 64) {
		    box.setFill(COLOR_64);
		    text.setText("64");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_LOW));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		else if (tile == 128) {
		    box.setFill(COLOR_128);
		    text.setText("128");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_MID));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		else if (tile == 256) {
		    box.setFill(COLOR_256);
		    text.setText("256");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_MID));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		else if (tile == 512) {
		    box.setFill(COLOR_512);
		    text.setText("512");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_MID));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		else if (tile == 1024) {
		    box.setFill(COLOR_1024);
		    text.setText("1024");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_HIGH));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		else if (tile == 2048) {
		    box.setFill(COLOR_2048);
		    text.setText("2048");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_HIGH));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		else if (tile == 4096) {
		    box.setFill(COLOR_OTHER);
		    text.setText("4096");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_HIGH));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		else if (tile == 8192) {
		    box.setFill(COLOR_OTHER);
		    text.setText("8192");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_HIGH));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		else if (tile == 16384) {
		    box.setFill(COLOR_OTHER);
		    text.setText("16384");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_HIGH));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		else if (tile == 32768) {
		    box.setFill(COLOR_OTHER);
		    text.setText("32768");
		    text.setFont(Font.font("Comic Sans", FontWeight.BOLD, 
				TEXT_SIZE_HIGH));
		    text.setFill(COLOR_VALUE_LIGHT);
		}

		//Updates score.
		scoreHolder = board.getScore();
		scoreText.setText(("Score: " + scoreHolder));
	    }
	}
    }

    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
    private void processArgs(String[] args)
    {
	String inputBoard = null;   // The filename for where to load the Board
	int boardSize = 0;          // The Size of the Board

	// Arguments must come in pairs
	if((args.length % 2) != 0)
	{
	    printUsage();
	    System.exit(-1);
	}

	// Process all the arguments 
	for(int i = 0; i < args.length; i += 2)
	{
	    if(args[i].equals("-i"))
	    {   // We are processing the argument that specifies
		// the input file to be used to set the board
		inputBoard = args[i + 1];
	    }
	    else if(args[i].equals("-o"))
	    {   // We are processing the argument that specifies
		// the output file to be used to save the board
		outputBoard = args[i + 1];
	    }
	    else if(args[i].equals("-s"))
	    {   // We are processing the argument that specifies
		// the size of the Board
		boardSize = Integer.parseInt(args[i + 1]);
	    }
	    else
	    {   // Incorrect Argument 
		printUsage();
		System.exit(-1);
	    }
	}

	// Set the default output file if none specified
	if(outputBoard == null)
	    outputBoard = "2048.board";
	// Set the default Board size if none specified or less than 2
	if(boardSize < 2)
	    boardSize = 4;

	// Initialize the Game Board
	try{
	    if(inputBoard != null)
		board = new Board(inputBoard, new Random());
	    else
		board = new Board(boardSize, new Random());
	}
	catch (Exception e)
	{
	    System.out.println(e.getClass().getName() + 
		    " was thrown while creating a " +
		    "Board from file " + inputBoard);
	    System.out.println("Either your Board(String, Random) " +
		    "Constructor is broken or the file isn't " +
		    "formated correctly");
	    System.exit(-1);
	}
    }

    // Print the Usage Message 
    private static void printUsage()
    {
	System.out.println("Gui2048");
	System.out.println("Usage:  Gui2048 [-i|o file ...]");
	System.out.println();
	System.out.println("  Command line arguments come in pairs of the "+ 
		"form: <command> <argument>");
	System.out.println();
	System.out.println("  -i [file]  -> Specifies a 2048 board that " + 
		"should be loaded");
	System.out.println();
	System.out.println("  -o [file]  -> Specifies a file that should be " + 
		"used to save the 2048 board");
	System.out.println("                If none specified then the " + 
		"default \"2048.board\" file will be used");  
	System.out.println("  -s [size]  -> Specifies the size of the 2048" + 
		"board if an input file hasn't been"); 
	System.out.println("                specified.  If both -s and -i" + 
		"are used, then the size of the board"); 
	System.out.println("                will be determined by the input" +
		" file. The default size is 4.");
    }
}
