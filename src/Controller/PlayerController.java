package Controller;

import GUI.Client;
import GUI.GameUIController;
import Module.GameBoard;
import Object.Player;
import javafx.animation.PauseTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.io.Serializable;

import static GUI.Client.outputData;
import static GUI.GameUIController.nameFieldStatic;
import static GUI.GameUIController.rollTheDiceBtnStatic;


public class PlayerController implements Serializable {
    private GameUIController gameUI;
    private GameBoard gameBoard;
    public int globalPosicion;

    public PlayerController(GameUIController gameUI, GameBoard gameBoard) {
        this.gameUI = gameUI;
        this.gameBoard = gameBoard;
    }

    public void rollDiceAndMove() throws IOException {
        int diceRoll = gameBoard.rollDice();
        gameUI.lockDiceButton();

        PauseTransition pause = new PauseTransition(Duration.seconds(GameBoard.DICE_ROLL_DELAY));

        pause.setOnFinished(event -> {
            gameUI.indicateDiceRoll(diceRoll);
            gameUI.resetAllTiles();
            gameUI.unlockDiceButton();

            int newIndex = getPlayerIndexAfterRoll(diceRoll);
            move(newIndex);

            if (newIndex == GameBoard.WIN_POINT) {
                pause.stop();
                gameUI.gameOver();
            } else {
//                swapTurns();
                gameUI.updateCurrentTurnLabel();
            }
            try {
                outputData.writeUTF("#PLAY-" + nameFieldStatic.getText() + "-" + newIndex);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            rollTheDiceBtnStatic.setVisible(false);
            rollTheDiceBtnStatic.setDisable(true);
        });
        pause.play();
    }

    public void move(int currentIndex) {
        int[] newCoordinates = gameBoard.getBoardCoordinates(GameBoard.NUMBER_OF_TILES - currentIndex);
        Pane clickedCell;
        clickedCell = (Pane) gameUI.getNodeByRowColumnIndex(newCoordinates[0], newCoordinates[1]);

        gameBoard.getCurrentPlayer().getPlayerPiece().setPosition(currentIndex);
        gameUI.movePieceImages(newCoordinates[0], newCoordinates[1]);

        if (currentIndex == GameBoard.WIN_POINT) {
            --currentIndex;							// silly way of avoiding the ArrayOutOfBoundsException.
        }

        if (gameBoard.getTile(currentIndex).containsLadderOrSnake()) {
            gameUI.setCellStyle(clickedCell, GameUIController.SNAKE_OR_LADDER_HIT_CELL_STYLE);
            int updatedIndex = gameBoard.getUpdatedPosition(currentIndex);
            move(updatedIndex);
            return;
        }
    }

    public int getPlayerIndexAfterRoll(int diceRoll) {
        Player currentPlayer = gameBoard.getCurrentPlayer();

        int currentPosition = currentPlayer.getPlayerPiece().getPosition();
        int newIndex = -1;

        newIndex = currentPosition + diceRoll;
        globalPosicion = newIndex;

        if (newIndex >= 100) {
            newIndex = 100;
//            newIndex = newIndex - 100;
        }
        return newIndex;
    }

    public void swapTurns() {
        if (gameBoard.getCurrentPlayer() == gameBoard.getPlayer1()) {
            gameBoard.getPlayer1().setTurn(0);
            gameBoard.getPlayer2().setTurn(1);
        } else if (gameBoard.getCurrentPlayer() == gameBoard.getPlayer2()) {
            gameBoard.getPlayer1().setTurn(1);
            gameBoard.getPlayer2().setTurn(0);
        }
    }
}
