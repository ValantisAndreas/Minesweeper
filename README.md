# About

- The project's code can be found inside the [minesweeper](https://github.com/ValantisAndreas/Minesweeper/tree/main/src/minesweeper) folder.
- Round Stats are stored inside the [recentgames](https://github.com/ValantisAndreas/Minesweeper/tree/main/src/recentgames) folder.
- Game Scenarios created by the player are stored inside the [medialab](https://github.com/ValantisAndreas/Minesweeper/tree/main/src/medialab) folder.
- Inside the [mines](https://github.com/ValantisAndreas/Minesweeper/tree/main/src/mines) folder a `mines.txt` is created everytime a new game is started and contains the row and column where each mine is located, as well as a third value that signals whether the mine is a super-mine or not.

# Game Rules

The game follows the same rules as the classic Minesweeper game with a few additions:

* The player first needs to Load a game ether from the pre-existing ones in the `Load` menu or create a new game scenario from the `Create` menu. When the game scenario is loaded press `Start` to beigin the game.
* The player can create their own gamplay scenarios which are then saved locally and can be loaded again and again without recreating them.
* Round stats are saved at the end of each and can be viewd at a later time.
* When the difficulty is set to `2` a `super-mine` is also initialized. If the player marks the square corresponding to the super-mine within the first 4 attempts (left-clicks) then automatically all the squares that are in the same row and column as the super-mine (31 total squares) are revealed.
* When the countdown reaches zero the game is over and the player is lost.
* FRom the `Solution` menu the current game's solution can be found, but the game will end and be marked as a loss for the player.

# Game description details

| Difficulty Levels | Grid Size | Number of Mines | Timer (seconds)| Super-Mine |
| ------------------| ----------| --------------- |----------------|------------|
| 1                 | 9x9       | 9-11            | 120-180        | No         |
| 2                 | 16x16     | 35-45           | 240-360        | Yes        |

# Screenshots

<img width="643" alt="Screenshot 2023-04-01 at 4 47 42 PM" src="https://user-images.githubusercontent.com/94286214/229292953-e08bd09b-4e36-41fd-a576-08e73a20392f.png"> <img width="363" alt="Screenshot 2023-04-01 at 4 44 20 PM" src="https://user-images.githubusercontent.com/94286214/229292936-e1137b32-f969-434e-be0b-ba9b947b7fea.png"> 
