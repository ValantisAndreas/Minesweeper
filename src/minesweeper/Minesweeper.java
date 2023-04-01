package minesweeper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Minesweeper is a JavaFX-based implementation of the classic Minesweeper game.
 * The application allows users to create, load, and start games, as well as 
 * displaying game details such as remaining time, total mines, and flags left.
 * The game also provides functionality to show the solution of the current game
 * and view statistics of the last 5 completed games. The game allows user to play 
 * with 2 different difficulties as well as set a super-mine in the hard difficulty.
 *
 * <p>
 * The Minesweeper class is the main entry point of the application and extends
 * the JavaFX Application class. It contains the following methods:
 * </p>
 *
 * <ul>
 *   <li>main: The main entry point of the Minesweeper application.</li>
 *   <li>start: Initializes the primary stage of the Minesweeper application, 
 *       sets the initial layout and displays the stage.</li>
 *   <li>menuBar: Creates and configures the menu bar for the Minesweeper 
 *       application, with options for creating, loading, starting, and exiting
 *       games, and displaying game details.</li>
 *   <li>headerBar: Creates and configures the header bar for the Minesweeper 
 *       application, which displays the remaining time, total mines, and flags
 *       left.</li>
 * </ul>
 *
 * <p>
 * Additionally, the class contains other methods that handle game logic and 
 * user interactions, such as updating flag counters, counting down the timer,
 * resetting variables for new games, and more.
 * </p>
 */
public class Minesweeper extends Application {

    public static HBox infoBox;
    public VBox mineBox, timeBox, markedBox;
    public Label mineCountLabel, markedCountLabel, timeLabel;
	public String selectedGame;

	public static int timer = 0, mineCount = 0, superMine, labelWidth;
	public static int round_winner, round_time = 0, round_mines, round_tries = 0;
	public static boolean superMineFlag = false;
	private static File minesFile = new File("./src/mines/mines.txt");

	BorderPane root = new BorderPane();
    VBox vBox = new VBox();
    Scene scene = new Scene(vBox);
	public static Label flagLabel;
    GameDescription check;
	RoundsStats round;
	static Timeline time;

	/**
	 * The main entry point of the Minesweeper application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
		deleteMinesFile();
	}

	/**
	 * Initializes the primary stage of the Minesweeper application, sets the
	 * initial layout and displays the stage.
	 *
	 * @param primaryStage the primary stage of the application
	 * @throws Exception if there is an error during the initialization process
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		vBox.getChildren().add(0, menuBar());
		primaryStage.setScene(scene);
		primaryStage.setTitle("MediaLab Minesweeper");
		primaryStage.setResizable(false);
		primaryStage.setMinWidth(240);
		primaryStage.setX(50);
		primaryStage.setY(50);
		primaryStage.show();
	}

	/**
	 * Resets all variables to their initial state when starting a new game. 
	 * This method clears the  game board, deletes the mines.txt file if it exists
	 * and initializes a new board with the appropriate dimensions.
	 */
	public void resetVariables() {
		Board.startGame = true;
		Board.firstClick = false;
		superMineFlag = false;
		round_tries = 0;
		if (minesFile.exists()) {
			minesFile.delete();
		}
		vBox.getChildren().clear();
		Board.tiles = new Tile[Board.gameWidth][Board.gameHeight];
		vBox.getChildren().addAll(menuBar(), headerBar(), new Board().createBoard());
		scene.getWindow().sizeToScene();
	}

	/**
	 * Creates and configures the menu bar for the Minesweeper application, with
	 * options for creating, loading, starting, and exiting games, and displaying
	 * game details like the solution of the current game or stats of previous games.
	 *
	 * @return the configured menu bar for the Minesweeper application
	 */
	public MenuBar menuBar() {

		MenuBar menuBar = new MenuBar();
		Menu applicationMenu = new Menu("Application");

		MenuItem createMenuItem = new MenuItem("Create");
		createMenuItem.setOnAction(event -> {
			// Display the create popup window and save the corresponding
			// description file
			Stage createStage = new Stage();
			createStage.setTitle("Create new game");

			// Create the UI elements for the create popup window
			Label nameLabel = new Label("SCENARIO-:");
			TextField nameTextField = new TextField();

			Label difficultyLabel = new Label("Difficulty:");
			TextField difficultyTextField = new TextField();

			Label minesLabel = new Label("Number of Mines:");
			TextField minesTextField = new TextField();

			Label timeLabel = new Label("Time in seconds:");
			TextField timeTextField = new TextField();

			Label superMineLabel = new Label("Super-Mine:");
			TextField superMineTextField = new TextField();

			Button saveButton = new Button("Save");

			// Create a vertical layout for the create popup window
			VBox createLayout = new VBox(10);
			createLayout.getChildren().addAll(nameLabel, nameTextField, difficultyLabel, difficultyTextField,
					minesLabel, minesTextField, timeLabel, timeTextField, superMineLabel, superMineTextField,
					saveButton);
			createLayout.setAlignment(Pos.CENTER);

			// Create a new scene and set it to the create popup window
			Scene createScene = new Scene(createLayout, 300, 350);
			createStage.setScene(createScene);
			createStage.setResizable(false);
			createStage.show();

			// Save the corresponding description file when the save button is clicked
			saveButton.setOnAction(saveEvent -> {
				String name = nameTextField.getText();
				String difficulty = difficultyTextField.getText();
				String mines = minesTextField.getText();
				String time = timeTextField.getText();
				String superMine = superMineTextField.getText();

				// Code to save the description file
				String newScenario = String.format("%s\n%s\n%s\n%s", difficulty, mines, time, superMine);
				File scenarioFile = new File("./src/medialab/SCENARIO-" + name + ".txt");
				if (scenarioFile.exists()) {
					scenarioFile.delete();
				}
				try (BufferedWriter writer = new BufferedWriter(
						new FileWriter("./src/medialab/SCENARIO-" + name + ".txt", true))) {
					writer.write(newScenario);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				createStage.close();
			});
		});

		MenuItem loadMenuItem = new MenuItem("Load");
		loadMenuItem.setOnAction(event -> {
			// Code to display the load popup window and load the corresponding description
			// file
			Stage loadStage = new Stage();
			loadStage.setTitle("Load game");

			// Create the UI elements for the load popup window
			Label loadLabel = new Label("Select a saved game to load:");
			ComboBox<String> loadComboBox = new ComboBox<>();
			Button loadButton = new Button("Load");

			// Retrieve saved game names and add them to the combo box
			File directory = new File("./src/medialab/");
			File[] files = directory.listFiles();
			for (File file : files) {
				if (file.isFile() && file.getName().endsWith(".txt")) {
					String fileName = file.getName().replaceFirst("[.][^.]+$", "");
					loadComboBox.getItems().add(fileName);
				}
			}

			// Create a vertical layout for the load popup window
			VBox loadLayout = new VBox(10);
			loadLayout.getChildren().addAll(loadLabel, loadComboBox, loadButton);
			loadLayout.setAlignment(Pos.CENTER);

			// Create a new scene and set it to the load popup window
			Scene loadScene = new Scene(loadLayout, 400, 300);
			loadStage.setScene(loadScene);
			loadStage.setResizable(false);
			loadStage.show();

			// Load the corresponding description file when the load button is clicked
			loadButton.setOnAction(loadEvent -> {
				selectedGame = loadComboBox.getSelectionModel().getSelectedItem();
				try {
					check = new GameDescription(selectedGame);
				} catch (FileNotFoundException | InvalidDescriptionException | InvalidValueException e) {
					e.printStackTrace();
				}

				loadStage.close();
			});
		});

		MenuItem startMenuItem = new MenuItem("Start");
		startMenuItem.setOnAction(event -> {
			Board.difficulty = check.getDifficultyLevel();
			timer = check.getMaxTime();
			superMine = check.hasSuperMine();
			Board.totalMines = check.getMines();
			mineCount = Board.totalMines;
			round_time = timer;

			if (Board.difficulty == 1) {
				Board.gameWidth = 9;
				Board.gameHeight = 9;
			} else {
				Board.gameWidth = 16;
				Board.gameHeight = 16;
			}

			if(Board.startGame) {
				time.stop();
			}
			resetVariables();
		});

		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setOnAction(event -> {
			deleteMinesFile();
			System.exit(0);
		});

		applicationMenu.getItems().addAll(createMenuItem, loadMenuItem, startMenuItem, new SeparatorMenuItem(),
				exitMenuItem);

		Menu detailsMenu = new Menu("Details");

		MenuItem roundsMenuItem = new MenuItem("Rounds");
		roundsMenuItem.setOnAction(event -> {
			// Code to display the details popup window for the last 5 completed games
			Stage roundsStage = new Stage();
			roundsStage.setTitle("Recent games stats");

			// Create the UI elements for the rounds popup window
			Label roundsLabel = new Label("Select a recent game to display:");
			ComboBox<String> roundsComboBox = new ComboBox<>();
			Button roundsButton = new Button("Display");

			// Retrieve the 5 most recent saved game names and add them to the combo box
			File directory = new File("./src/recentgames/");
			File[] files = directory.listFiles();
			int fileCount = files != null ? files.length : 0;
			if (fileCount > 0) {
				// sort in descending order
				Arrays.stream(files)
						.sorted(Comparator.comparing(File::getName).reversed())
						.limit(5)
						.map(file -> file.getName().replaceFirst("[.][^.]+$", ""))
						.forEach(fileName -> roundsComboBox.getItems().add(fileName));
			}

			// Create a vertical layout for the load popup window
			VBox roundsLayout = new VBox(10);
			roundsLayout.getChildren().addAll(roundsLabel, roundsComboBox, roundsButton);
			roundsLayout.setAlignment(Pos.CENTER);

			// Create a new scene and set it to the load popup window
			Scene roundsScene = new Scene(roundsLayout, 400, 300);
			roundsStage.setScene(roundsScene);
			roundsStage.setResizable(false);
			roundsStage.show();

			// Load the corresponding description file when the load button is clicked
			roundsButton.setOnAction(roundsEvent -> {

				selectedGame = roundsComboBox.getSelectionModel().getSelectedItem();
				try {
					round = new RoundsStats(selectedGame);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				int minesTotal = round.getMinesTotal();
				int NoTries = round.getTries();
				int Time = round.getTime();
				int winner = round.getWinner();
				String winner_str = (winner == 0) ? "Computer" : "Player";

				// Display the create popup window and save the corresponding
				// description file
				Stage createStage = new Stage();
				createStage.setTitle(selectedGame);

				// Create the UI elements for the create popup window
				Label minesTotalLabel = new Label("Number of Mines: " + minesTotal);
				Label NoTriesLabel = new Label("Number of Tries: " + NoTries);
				Label TimeLabel = new Label("Time in seconds: " + Time);
				Label winnerLabel = new Label("Winner: " + winner_str);

				// Create a vertical layout for the create popup window
				VBox createLayout = new VBox(10);
				createLayout.getChildren().addAll(minesTotalLabel, NoTriesLabel, TimeLabel, winnerLabel);
				createLayout.setAlignment(Pos.CENTER);

				// Create a new scene and set it to the create popup window
				Scene createScene = new Scene(createLayout, 300, 350);
				createStage.setScene(createScene);
				createStage.show();
				roundsStage.close();
			});

		});

		MenuItem solutionMenuItem = new MenuItem("Solution");
		solutionMenuItem.setOnAction(event -> {
			if (Board.firstClick){
				Board.revealAllMines();
				Board.startGame = false;
				round_winner = 0;
				roundFile();
			}
		});

		detailsMenu.getItems().addAll(roundsMenuItem, solutionMenuItem);
		menuBar.getMenus().addAll(applicationMenu, detailsMenu);
		return menuBar;
	}

	/**
	 * Creates and configures the header bar for the Minesweeper application, which
	 * displays the remaining time, total mines, and flags left.
	 *
	 * @return the configured header bar for the Minesweeper application
	 */
	public HBox headerBar() {
		// Labels Bar
		labelWidth = ((Board.gameWidth * Tile.width) - Tile.width) / 3;
		infoBox = new HBox();

		// updateFlagCounter();
		flagLabel = new Label("Flags Left:" + Board.totalMines);
		flagLabel.setMinWidth(labelWidth);
		flagLabel.setMaxWidth(labelWidth);
		flagLabel.setMinHeight(Tile.height);
		flagLabel.setMaxHeight(Tile.height);
		flagLabel.setAlignment(Pos.CENTER_RIGHT);

		Label mineLabel = new Label("Total Mines: " + mineCount);
		mineLabel.setMinWidth(labelWidth);
		mineLabel.setMaxWidth(labelWidth);
		mineLabel.setMinHeight(Tile.height);
		mineLabel.setMaxHeight(Tile.height);

		mineLabel.setAlignment(Pos.CENTER);

		timeLabel = new Label("Time Left: " + timer);
		timeLabel.setMinWidth(labelWidth);
		timeLabel.setMaxWidth(labelWidth);
		timeLabel.setMinHeight(Tile.height);
		timeLabel.setMaxHeight(Tile.height);

		timeLabel.setAlignment(Pos.CENTER);

		infoBox.getChildren().addAll(timeLabel, mineLabel, flagLabel);
		countDown();
		return infoBox;
	}

	/**
	 * Updates the flag counter displayed in the Minesweeper game. This method
	 * removes the existing flag counter label and adds an updated one 
	 * with the current number of flags left.
	 */
	public static void updateFlagCounter() {
		Minesweeper.infoBox.getChildren().remove(Minesweeper.flagLabel);
		flagLabel = new Label("Flags Left:" + Board.totalMines);
		flagLabel.setMinWidth(labelWidth);
		flagLabel.setMaxWidth(labelWidth);
		flagLabel.setMinHeight(Tile.height);
		flagLabel.setMaxHeight(Tile.height);
		flagLabel.setAlignment(Pos.CENTER_RIGHT);
		Minesweeper.infoBox.getChildren().add(Minesweeper.flagLabel);
	}

	/**
	 * Sets up and starts a countdown timer in the Minesweeper game. 
	 * The timer counts down every second and displays the time left. 
	 * If the timer reaches zero, the game is lost and the
	 * appropriate alerts and messages are displayed.
	 */
	public void countDown() {
		time = new Timeline();
		time.setCycleCount(Timeline.INDEFINITE);

		if (time != null) {
			time.stop();
		}

		KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
			// @Override
			public void handle(ActionEvent event) {
				timer--;
				timeLabel.setText("Time Left: " + timer);
				if (timer == 0) {
					time.stop();
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Time's up! You lost!");
					alert.show();
					round_winner = 0;
					Board.startGame = false;
					roundFile();
					System.out.println("You Lost!");
				}
			}
		});

		time.getKeyFrames().add(frame);
		time.playFromStart();

	}

	/**
	 * Creates a round stats file containing the game statistics.
	 * Saves the total number of mines, the number of tries, the time used, and the
	 * winner of the round.
	 */
	private static void roundFile() {
		time.stop();
		round_mines = mineCount;
		int time_used;
		time_used = (timer == 0) ? round_time : (round_time - timer);

		// Code to save the game stats file
		String newGame = String.format("%d\n%d\n%d\n%d", round_mines, round_tries, time_used, round_winner);

	    // Get the current date and time
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
		String formattedDateTime = now.format(formatter);

		try (BufferedWriter writer = new BufferedWriter(
				new FileWriter("./src/recentgames/Game-" + formattedDateTime + "-Stats.txt", true))) {
			writer.write(newGame);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a mines.txt file containing each mine's (x,y) coordinates and wheter 
	 * it is a super-mine (1) or not (0).
	 *
	 * @param row       the row index of the mine
	 * @param column    the column index of the mine
	 * @param supermine the supermine status (1 for supermine, 0 for regular mine)
	 */
	public static void minesFile(int row, int column, int supermine) {
		// Code to save the description file
		String minesfile = String.format("%d, %d, %d\n", row, column, supermine);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/mines/mines.txt", true))) {
			writer.write(minesfile);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes the mines.txt file upon application shutdown or a new game starts.
	 */
	private static void deleteMinesFile() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (minesFile.exists()) {
					minesFile.delete();
				}
			}
		});
	}

	/**
	 * Displays a popup window with the game result when the game ends.
	 *
	 * @param winner the winner of the game (1 for win, 0 for loss)
	 * @param msg    the message to display in the popup window
	 */
	public static void endGamePopUp(int winner, String msg) {
		Board.startGame = false;
		round_winner = winner;
		roundFile();
		System.out.println(msg);
		Stage resultStage = new Stage();
		// Create the UI elements for the result popup window
		Label resultLabel = new Label(msg);
		// Create a vertical layout for the result popup window
		VBox resultLayout = new VBox(10);
		resultLayout.getChildren().addAll(resultLabel);
		resultLayout.setAlignment(Pos.CENTER);
		// Create a new scene and set it to the result popup window
		Scene resultScene = new Scene(resultLayout, 90, 50);
		resultStage.setScene(resultScene);
		resultStage.show();
	}

}