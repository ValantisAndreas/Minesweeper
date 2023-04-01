package minesweeper;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

/**
 * Represents the Minesweeper game board and manages its state.
 * 
 * <p>
 * The Board class contains methods to initialize and manipulate the game board,
 * handle click events, reveal tiles, and check the game state. It uses a 2D array
 * of Tile objects to represent the board's cells and keeps track of various game
 * parameters such as difficulty, game dimensions, and the total number of mines.
 * </p>
 * 
 * @author Χρυσοβαλάντης-Κων/νος Ανδρεάς
 * @version 1.0
 */
public class Board {
    public static int difficulty, gameWidth = 1, gameHeight = 1,  totalMines = 0;
    public static int superMineX, superMineY;
    public static boolean startGame = false, firstClick = false;
    public static Tile tiles[][];

    /**
     * Creates a GridPane representing the Minesweeper game board.
     * 
     * @return A GridPane containing Tile objects as cells.
     */
    public GridPane createBoard() {
        GridPane board = new GridPane();
        for (int col = 0; col < gameHeight; col++) {
            for (int row = 0; row < gameWidth; row++) {
                Tile tile = new Tile();
                tiles[row][col] = tile;
                tile.setTileState(0);
                tile.setOnMouseClicked(e -> {
                    if (startGame) {
                        int clickedX = GridPane.getColumnIndex(tile);
                        int clickedY = GridPane.getRowIndex(tile);
                        if (e.getButton() == MouseButton.PRIMARY) {
                            if (!firstClick) {
                                setMines(totalMines, clickedX, clickedY);
                                checkBlanks(clickedX, clickedY);
                                firstClick = true;
                                Minesweeper.round_tries++;
                            } else {
                                handlePrimaryClick(tile);
                            }
                        }
                        if (e.getButton() == MouseButton.SECONDARY) {
                            handleSecondaryClick(tile);
                        }
                    }
                });

                board.add(tile, row, col);
            }
        }
        return board;
    }

    /**
     * Handles primary (left) mouse click events on the Minesweeper board.
     * 
     * @param tile The Tile object that was clicked.
     */
    private void handlePrimaryClick(Tile tile) {
        Minesweeper.round_tries++;

        if (tile.getGraphic() == tile.getFlag()) {
            handleSecondaryClick(tile);
        }

        if (tile.getTileState() == Tile.MINE) { // Tile has mine
            revealAllMines();
            tile.setGraphic(tile.getExplosion());
            Minesweeper.endGamePopUp(0, "You Lost!");
        } 
        else {
            if (tile.getTileState() == Tile.NUMBERED) { // Tile is surrounded by mine(s)
                if (tile.getGraphic() != null) {
                    tile.setGraphic(null);
                    tile.setText("" + tile.getSurroundingMines());
                    if (areAllNumberedTilesUncovered()) {
                        Minesweeper.endGamePopUp(1, "You Win!");
                    }
                } 
            } 
            else {
                if (tile.getTileState() == Tile.BLANK) { // Tile is blank
                    int clickedX = GridPane.getColumnIndex(tile);
                    int clickedY = GridPane.getRowIndex(tile);
                    checkBlanks(clickedX, clickedY);
                }
            }
        }
    }

    /**
     * Handles secondary (right) mouse click events on the Minesweeper board
     * by placing or removing the flag on the tile that was clicked.
     * 
     * @param tile The Tile object that was clicked.
     */
    private void handleSecondaryClick(Tile tile) {
        if (tile.getGraphic() == tile.getFlag()) {
            tile.setGraphic(tile.getTile());
            totalMines++;
            Minesweeper.updateFlagCounter();
        } else {
            if (totalMines > 0 && tile.getGraphic() == tile.getTile()) {
                tile.setGraphic(tile.getFlag());
                totalMines--;
                Minesweeper.updateFlagCounter();
                if (firstClick && Minesweeper.superMineFlag && Minesweeper.round_tries <= 4) {
                    int x = GridPane.getRowIndex(tile);
                    int y = GridPane.getColumnIndex(tile);
                    if (x == superMineX && y == superMineY) {
                        revealSuperMine(y, x);
                    }
                }
            }
        }
    }

    /**
     * Reveals the contents of all squares in the same row and column as the
     * super-mine
     * if the player marks the super-mine within the first 4 attempts.
     *
     * @param superMineX The x-coordinate of the super-mine on the board
     * @param superMineY The y-coordinate of the super-mine on the board
     */
    private void revealSuperMine(int superMineX, int superMineY) {
    
        for (int x = 0; x < gameWidth; x++) {
            revealTile(x, superMineY);
        }

        for (int y = 0; y < gameHeight; y++) {
            if (y != superMineY) { // Avoid revealing the super-mine again
                revealTile(superMineX, y);
            }
        }
    }

    /**
     * Reveals the content of a tile and updates its state accordingly.
     *
     * @param x The x-coordinate of the tile on the board
     * @param y The y-coordinate of the tile on the board
     */
    private void revealTile(int x, int y) {
        Tile tile = tiles[x][y];
        if (tile.getGraphic() == tile.getFlag()) {
            totalMines++;
            Minesweeper.updateFlagCounter();
        }
        if (tile.getTileState() == Tile.MINE) { // Tile contains a mine
            tile.setGraphic(tile.getMine());
            tile.setDisable(true);
            totalMines--;
            Minesweeper.updateFlagCounter();
        } else if (tile.getTileState() == Tile.BLANK) { // Tile is blank
            tile.setGraphic(null);
        } else { // Tile is a numbered tile
            tile.setGraphic(null);
            tile.setText("" + tile.getSurroundingMines());
        }
    }

    /**
     * Recursively checks and reveals blank tiles and their adjacent numbered tiles.
     *
     * @param row    The x-coordinate of the tile to check.
     * @param column The y-coordinate of the tile to check.
     */
    private void checkBlanks(int row, int column) {
        if (row < 0 || row >= gameWidth || column < 0 || column >= gameHeight) {
            return;
        }

        if (tiles[row][column].getSurroundingMines() == 0
                && tiles[row][column].getGraphic() == tiles[row][column].getTile()) {
            tiles[row][column].setGraphic(null);

            checkBlanks(row + 1, column);
            checkBlanks(row - 1, column);
            checkBlanks(row, column + 1);
            checkBlanks(row, column - 1);
        }

        if (tiles[row][column].getSurroundingMines() != 0
                && tiles[row][column].getGraphic() == tiles[row][column].getTile()) {
            tiles[row][column].setGraphic(null);
            tiles[row][column].setText(Integer.toString(tiles[row][column].getSurroundingMines()));
        }

    }

    /**
     * Reveals all mines on the grid as unexploded mines.
     */
    public static void revealAllMines() {
        for (int row = 0; row < gameWidth; row++) {
            for (int col = 0; col < gameHeight; col++) {
                if (tiles[row][col].getTileState() == Tile.MINE) {
                    tiles[row][col].setGraphic(tiles[row][col].getMine());
                }
            }
        }
    }

    /**
     * Checks if all numbered tiles are uncovered.
     *
     * @return true if all numbered tiles are uncovered, false otherwise.
     */
    private boolean areAllNumberedTilesUncovered() {
        for (int row = 0; row < gameWidth; row++) {
            for (int col = 0; col < gameHeight; col++) {
                if (tiles[row][col].getTileState() == Tile.NUMBERED && tiles[row][col].getGraphic() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method to randomly set the bombs for the game.
     * 
     * @param mines  The total number of mines to be placed on the board.
     * @param row    The x-coordinate of the player's first click.
     * @param column The y-coordinate of the player's first click.
     */
    private void setMines(int mines, int row, int column) {
        int counter = 0;
        java.util.Random random = new java.util.Random();
        while (counter < totalMines) {
            int j = random.nextInt(gameHeight);
            int i = random.nextInt(gameWidth);
            try {
                if (!firstClick) {
                    ensureFirstClickSafety(row, column);
                    firstClick = true;
                }

                if (tiles[i][j].getTileState() != Tile.MINE && !tiles[i][j].getNoMine()) {
                    tiles[i][j].setTileState(-1);
                    counter++;
                    setNumbers(i, j);

                    // Place the super-mine and set its supermine parameter as 1
                    if (!Minesweeper.superMineFlag && difficulty == 2) {
                        Minesweeper.minesFile(j, i, 1);
                        Minesweeper.superMineFlag = true;
                        superMineX = j;
                        superMineY = i;

                    } else {
                        Minesweeper.minesFile(j, i, 0);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
            }
        }
        MarkSafeTiles();
    }

    /**
     * Ensures that the tiles surrounding the player's first click are safe (no
     * mines).
     * 
     * @param row    The x-coordinate of the player's first click.
     * @param column The y-coordinate of the player's first click.
     */
    private void ensureFirstClickSafety(int row, int column) {
        int[][] neighbors = {
                { -1, -1 }, { -1, 0 }, { -1, 1 },
                { 0, -1 }, { 0, 1 },
                { 1, -1 }, { 1, 0 }, { 1, 1 }
        };

        for (int[] neighbor : neighbors) {
            int x = row + neighbor[0];
            int y = column + neighbor[1];

            if (isValidCoordinate(x, y)) {
                tiles[x][y].setNoMine();
            }
        }
    }

    /**
     * Checks if the given coordinates are within the bounds of the game board.
     * 
     * @param x The x-coordinate to be checked.
     * @param y The y-coordinate to be checked.
     * @return True if the coordinates are valid, false otherwise.
     */
    private boolean isValidCoordinate(int row, int column) {
        return row >= 0 && row < gameWidth && column >= 0 && column < gameHeight;
    }

    /**
     * Marks the safe tiles on the game board.
     * Safe tiles are those that do not have a mine.
     */
    private void MarkSafeTiles() {
        for (int column = 0; column < gameHeight; column++) {
            for (int row = 0; row < gameWidth; row++) {
                if (tiles[row][column].getNoMine()) {
                    checkBlanks(row, column);
                    tiles[row][column].setGraphic(null);
                    if (tiles[row][column].getSurroundingMines() != 0) {
                        tiles[row][column].setText("" + tiles[row][column].getSurroundingMines());
                    }
                }
            }
        }
    }

    /**
     * Sets the numbers of the surrounding tiles around a mine at the specified
     * coordinates (x, y).
     * The method increments the number of each adjacent tile, if it's not a mine
     * itself.
     * 
     * @param row The x-coordinate of the mine.
     * @param column The y-coordinate of the mine.
     */
    private void setNumbers(int row, int column) {
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetY = -1; offsetY <= 1; offsetY++) {
                int newX = row + offsetX;
                int newY = column + offsetY;

                if (isValidCoordinate(newX, newY) && tiles[newX][newY].getTileState() != -1) {
                    tiles[newX][newY].setTileState(1);
                }
            }
        }
        areAllNumberedTilesUncovered();
    }

}