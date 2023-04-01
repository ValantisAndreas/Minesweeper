package minesweeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A class that represents the statistics of a single game round of the Minesweeper game.
 * It reads 4 values from a text file whose name is specified in the "gameID" parameter.
 * The values represent the total number of mines in the game,
 * the number of tries (left clicks) the player made during the game,
 * the duration of the game and who won the game, computer or player.
 * If the specified file is not found, a FileNotFoundException will be thrown.
 * 
 * @author Χρυσοβαλάντης-Κων/νος Ανδρεάς
 * @version 1.0
 */

public class RoundsStats {
    private int minesTotal; //Number of mines in the game
    private int NoTries; //Number of tries (left clicks) the player made during the game
    private int time; // Duration of the game 
    private int winner; // who won the game, computer or player

    /**
     * Constructs a new RoundsStats object by reading the game statistics from a file with the specified game ID.
     * @param gameID the ID of the game file to read the statistics from
     * @throws FileNotFoundException if the specified file is not found
     */
    public RoundsStats(String gameID)
            throws FileNotFoundException {
        File file = new File("./src/recentgames/" + gameID + ".txt");
        Scanner scanner = new Scanner(file);
        try {
            minesTotal = Integer.parseInt(scanner.nextLine());
            NoTries = Integer.parseInt(scanner.nextLine());
            time = Integer.parseInt(scanner.nextLine());
            winner = Integer.parseInt(scanner.nextLine());
        } finally {
            scanner.close();
        }

    }

    /**
     * Returns the total number of mines in the game.
     * @return the total number of mines in the game
     */
    public int getMinesTotal() {
        return minesTotal;
    }

    /**
     * Returns the number of tries (left clicks) the player made during the game.
     * @return the number of tries (left clicks) the player made during the game
     */
    public int getTries() {
        return NoTries;
    }

    /**
     * Returns the duration of the game.
     * @return the duration of the game
     */
    public int getTime() {
        return time;
    }

    /**
     * Returns the winner of the game, either the computer or the player.
     * @return 0 if the computer won, 1 if the player won
     */
    public int getWinner() {
        return winner;
    }
}