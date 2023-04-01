package minesweeper;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A Tile represents a square in the Minesweeper game board.
 * It extends JavaFX Button class, and contains images for different tile states.
 * The Tile class also contains methods to get the state of the tile and set the flag.
 * 
 * @author Χρυσοβαλάντης-Κων/νος Ανδρεάς
 * @version 1.0
*/
public class Tile extends Button {
	
	private ImageView imageFlag, imageMine, imageExplosion, imageOpen, imageTile;
	private int tileState; 
	private Boolean noMine = false;
	private int surroundingMines = 0;
	public static final int MINE = -1, BLANK = 0, NUMBERED = 1;
	final static int width = 40, height = 40;

	/**
	 * Constructs a Tile object with default values.
	 * Sets dimensions of the tile to a fixed value of 50x50 pixels.
	 * Initializes images for different tile states.
	 */ 
	public Tile() {

		this.setMinWidth(width);
		this.setMinHeight(height);
		this.setMaxWidth(width);
		this.setMaxHeight(height);

		imageFlag = new ImageView(new Image("file:Images/Flag.png"));
		imageFlag.setFitHeight(height);
		imageFlag.setFitWidth(width);

		imageMine = new ImageView(new Image("file:Images/Mine.png"));
		imageMine.setFitHeight(height);
		imageMine.setFitWidth(width);

		imageExplosion = new ImageView(new Image("file:Images/Explosion.png"));
		imageExplosion.setFitHeight(height);
		imageExplosion.setFitWidth(width);

		imageOpen = new ImageView(new Image("file:Images/Open.png"));
		imageOpen.setFitHeight(height);
		imageOpen.setFitWidth(width);

		imageTile = new ImageView(new Image("file:Images/Tile.png"));
		imageTile.setFitHeight(height);
		imageTile.setFitWidth(width);

		setGraphic(imageTile);

		tileState = 1;
	}

	/**
	 * Returns the image used for a flagged tile.
	 * @return imageFlag ImageView of the flagged tile.
	 */
	public ImageView getFlag() {
		return imageFlag;
	}

	/**
	 * Returns the image used for a mine tile.
	 * @return imageMine ImageView of the mine tile.
	 */
	public ImageView getMine() {
		return imageMine;
	}

	/**
	 * Returns the image used for an exploded mine tile.
	 * @return imageExplosion ImageView of the exploded mine tile.
	 */
	public ImageView getExplosion() {
		return imageExplosion;
	}

	/**
	 * Returns the image used for an open tile.
	 * @return imageOpen ImageView of the open tile.
	 */
	public ImageView getOpen() {
		return imageOpen;
	}

	/**
	 * Returns the image used for a closed tile.
	 * @return imageTile ImageView of the closed tile.
	 */
	public ImageView getTile() {
		return imageTile;
	}

	/**
	 * Returns a Boolean indicating whether the tile contains a mine.
	 * @return noMine Boolean indicating whether the tile contains a mine.
	 */
	public Boolean getNoMine() {
		return noMine;
	}

	/**
	 * Sets the tile to have no mine.
	 */
	public void setNoMine() {
		noMine = true;
	}

	/** Returns the state of the tile.
	 * @return The state of the tile (-1: Mine, 0: Blank, 1: Numbered -> surrounded by mine(s)).
	 */
	public int getTileState() {
		return tileState;
	}

	/** Returns the number of mines surrounding the tile.
	 * @return The number of surrounding mines.
	 */
	public int getSurroundingMines() {
		return this.surroundingMines;
	}

	/**
	 * Sets the state of the tile.
	 * @param state The state of the tile (-1: Mine, 0: Blank, 1: Numbered -> surrounded by mine(s)).
	 */
	public void setTileState(int state) {
		this.tileState = state;
		this.surroundingMines += (state == 1) ? 1 : 0;
	}

}