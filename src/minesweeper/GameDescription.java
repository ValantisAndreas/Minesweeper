package minesweeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.scene.control.Alert;

/**
 * The GameDescription class is responsible for checking the validity of the game description values
 * specified in the scenarioID txt file. It contains methods for retrieving the difficulty level,
 * total number of mines, maximum time allowed, and whether or not super mines are allowed in the game.
 * It also contains a method for displaying an error message if an invalid value or file format is detected.
 * The constructor takes in a scenarioID string, reads the corresponding txt file, and checks the validity of
 * the values specified in the file. If any of the values are invalid, an appropriate exception is thrown.
 * The class also contains two custom exceptions: InvalidDescriptionException and InvalidValueException.
 * InvalidDescriptionException is thrown when the game description file format is invalid. InvalidValueException
 * is thrown when any of the values specified in the game description file are invalid.
 * 
 * @author Χρυσοβαλάντης-Κων/νος Ανδρεάς
 * @version 1.0
*/
public class GameDescription {
    private int difficultyLevel;
    private int mines;
    private int maxTime;
    private int hasSuperMine;
    private String message;
    
    /**
     * Constructs a GameDescription object by reading the scenarioID.txt file 
     * and setting the difficultyLevel, mines, maxTime, hasSuperMine, and message fields.
     * @param scenarioID the scenario ID used to locate the txt file
     * @throws FileNotFoundException if the txt file is not found
     * @throws InvalidDescriptionException if the txt file has an invalid format
     * @throws InvalidValueException if any of the game description values are invalid
     */
    public GameDescription(String scenarioID)
            throws FileNotFoundException, InvalidDescriptionException, InvalidValueException {
        File file = new File("./src/medialab/" + scenarioID + ".txt");
        Scanner scanner = new Scanner(file);
        try {
            if (!scanner.hasNextLine()) {
                message = "Invalid game description file format";
                errorDisplay(message);
                throw new InvalidDescriptionException(message);
            }

            try {
                difficultyLevel = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                message = "Invalid difficulty level value";
                errorDisplay(message);
                throw new InvalidValueException(message);
            }

            if (difficultyLevel != 1 && difficultyLevel != 2) {
                message = "Invalid difficulty level value: " + difficultyLevel;
                errorDisplay(message);
                throw new InvalidValueException(message);
            }

            if (!scanner.hasNextLine()) {
                message = "Invalid game description file format";
                errorDisplay(message);
                throw new InvalidDescriptionException(message);
            }

            try {
                mines = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                message = "Invalid total mines value";
                errorDisplay(message);
                throw new InvalidValueException(message);
            }

            if (difficultyLevel == 1 && (mines < 9 || mines > 11)) {
                message = "Invalid total mines value: " + mines;
                errorDisplay(message);
                throw new InvalidValueException(message);
            } else if (difficultyLevel == 2 && (mines < 35 || mines > 45)) {
                message = "Invalid total mines value: " + mines;
                errorDisplay(message);
                throw new InvalidValueException(message);
            }

            if (!scanner.hasNextLine()) {
                message = "Invalid game description file format";
                errorDisplay(message);
                throw new InvalidDescriptionException(message);
            }

            try {
                maxTime = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                message = "Invalid maximum time value";
                errorDisplay(message);
                throw new InvalidValueException(message);
            }

            if (difficultyLevel == 1 && (maxTime < 120 || maxTime > 180)) {
                message = "Invalid maximum time value: " + maxTime;
                errorDisplay(message);
                throw new InvalidValueException(message);
            } else if (difficultyLevel == 2 && (maxTime < 240 || maxTime > 360)) {
                message = "Invalid maximum time value: " + maxTime;
                errorDisplay(message);
                throw new InvalidValueException(message);
            }

            if (!scanner.hasNextLine()) {
                message = "Invalid game description file format";
                errorDisplay(message);
                throw new InvalidDescriptionException(message);
            }

            try {
                hasSuperMine = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                message = "Invalid super-mine value";
                errorDisplay(message);
                throw new InvalidValueException(message);
            }

            if (difficultyLevel == 1 && hasSuperMine == 1) {
                message = "Super-mine not allowed in level 1";
                errorDisplay(message);
                throw new InvalidValueException(message);
            }
        } finally {
            scanner.close();
        }

    }

    /**
     * Returns the difficulty level of the game.
     * @return the difficulty level
     */
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    /**
     * Returns the total number of mines in the game.
     * @return the total number of mines
     */
    public int getMines() {
        return mines;
    }

    /**
     * Returns the maximum time allowed for the game.
     * @return the maximum time in seconds
     */
    public int getMaxTime() {
        return maxTime;
    }

    /**
     * Returns 1 if the game has a super-mine, 0 otherwise.
     * @return 1 if the game has a super-mine, 0 otherwise
     */
    public int hasSuperMine() {
        return hasSuperMine;
    }

    /**
     * Displays an error message to the user in an alert window.
     * @param error_msg the error message to display
     */
    public void errorDisplay(String error_msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Error Detected! " + error_msg);
        alert.show();
    }
}

/**
 * Thrown to indicate that a description is invalid.
*/
class InvalidDescriptionException extends Exception {
    /**
     *  Constructs a new InvalidDescriptionException with the specified detail message.
     * @param message the detail message
     */
    public InvalidDescriptionException(String message) {
        super(message);
    }
}

/**
 * Thrown to indicate that a value is invalid.
*/
class InvalidValueException extends Exception {
    /**
     * Constructs a new InvalidValueException with the specified detail message.
     * @param message the detail message
     */
    public InvalidValueException(String message) {
        super(message);
    }
}