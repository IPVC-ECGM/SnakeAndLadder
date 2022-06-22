package GUI;

import Controller.PlayAgainOrExitAlert;
import Controller.PlayerController;
import Controller.ResourcesLoader;
import Module.GameBoard;
import Object.HumanPlayer;
import Object.Piece;
import Object.Player;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;


public class GameUIController implements Initializable, Runnable, Serializable{

    @FXML
    private GridPane gameGrid;
    @FXML
    private ImageView boardImage;
    @FXML
    private Label currentTurnLabel;
    @FXML
    public Label currentTurnLabelStatic;
    @FXML
    private Label infoLabel;
    @FXML
    public static Label infoLabelStatic;
    @FXML
    public TextField nameField;
    @FXML
    private TextField ipInput;
    @FXML
    private TextField portInput;
    @FXML
    public static TextField nameFieldStatic;
    @FXML
    public Button logoutButton;
    @FXML
    public Button localPlay;
    @FXML
    public static Button localPlayStatic;
    @FXML
    public static Button logoutButtonStatic;
    @FXML
    public static Label backPane;
    @FXML
    private Label playerName;
    @FXML
    public static Label playerNameStatic;
    @FXML
    private Label adversaryName;
    @FXML
    public static Label adversaryNameStatic;
    @FXML
    private Button rollTheDiceBtn;
    @FXML
    public static Button rollTheDiceBtnStatic;
    @FXML
    private Label numberRolledLabel;
    @FXML
    private ImageView diceIndicatorImage;
    @FXML
    private ImageView nameBoard;
    @FXML
    private ImageView adversaryBoard;
    @FXML
    private ImageView infoBoard;
    @FXML
    private ImageView logoBoard;
    @FXML
    private ImageView chooseBluePieceImage;
    @FXML
    private ImageView chooseGreenPieceImage;
    @FXML
    private ImageView chooseRedPieceImage;
    @FXML
    private ImageView chooseYellowPieceImage;
    @FXML
    public static ImageView chooseBluePieceImageStatic;
    @FXML
    public static ImageView chooseGreenPieceImageStatic;
    @FXML
    public static ImageView chooseRedPieceImageStatic;
    @FXML
    public static ImageView chooseYellowPieceImageStatic;

    private final String DEFAULT_TILE_STYLE = "-fx-background-color:  Transparent";
    public static final String SNAKE_OR_LADDER_HIT_CELL_STYLE = "-fx-background-color: #D93636";
    private final Image BLUE_PIECE_IMAGE = ResourcesLoader.getImage("Pawns _blue.png");
    private final Image GREEN_PIECE_IMAGE   = ResourcesLoader.getImage("Pawns _gree.png");
    private final Image RED_PIECE_IMAGE   = ResourcesLoader.getImage("Pawns _red.png");
    private final Image YELLOW_PIECE_IMAGE   = ResourcesLoader.getImage("Pawns _yellow.png");
    private final Image BLACK_PIECE_IMAGE   = ResourcesLoader.getImage("Pawns _black.png");
    private final Image ROLL_THE_DICE_IMAGE = ResourcesLoader.getImage("dice.png");
    public static ImageView inGameBluePieceImage, inGameGreenPieceImage, inGameRedPieceImage, inGameYellowPieceImage, inGameBlackImage;
    private GameBoard gameBoard;
    public static GameBoard gb;
    private PlayerController playerController;
    public static PlayerController playCTR;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initializeStatus();
        } catch (Exception e) {
            e.printStackTrace();
            reportError(e.toString());
        }
    }

    public void initializeStatus() {
        colorTiles();
        gameBoard = new GameBoard();
        gb = gameBoard;
        final Piece yellowPiece = new Piece(GameBoard.PLAYER_STARTING_POSITION, "Yellow", inGameYellowPieceImage);
        final Piece blackPiece = new Piece(GameBoard.PLAYER_STARTING_POSITION, "Black", inGameBlackImage);
        Player player1 = new HumanPlayer(1, yellowPiece);
        Player player2 = new HumanPlayer(0, blackPiece);
        gameBoard.setPlayer1(player1);
        gameBoard.setPlayer2(player2);
        nameFieldStatic = nameField;
        logoutButtonStatic = logoutButton;
        playerNameStatic = playerName;
        adversaryNameStatic = adversaryName;
        currentTurnLabelStatic = currentTurnLabel;
        infoLabelStatic = infoLabel;
        localPlayStatic = localPlay;
        rollTheDiceBtnStatic = rollTheDiceBtn;
        chooseBluePieceImageStatic = chooseBluePieceImage;
        chooseRedPieceImageStatic = chooseRedPieceImage;
        chooseGreenPieceImageStatic = chooseGreenPieceImage;
        chooseYellowPieceImageStatic = chooseYellowPieceImage;
        playerController = new PlayerController(this, gameBoard);
        playCTR = playerController;
        inGameBluePieceImage = new ImageView(BLUE_PIECE_IMAGE);
        inGameGreenPieceImage = new ImageView(GREEN_PIECE_IMAGE);
        inGameRedPieceImage = new ImageView(RED_PIECE_IMAGE);
        inGameYellowPieceImage = new ImageView(YELLOW_PIECE_IMAGE);
        inGameBlackImage = new ImageView(BLACK_PIECE_IMAGE);
        rollTheDiceBtn.setGraphic(new ImageView(ROLL_THE_DICE_IMAGE));
        nameBoard.setImage(ResourcesLoader.getImage("textBanner.png"));
        adversaryBoard.setImage(ResourcesLoader.getImage("textBanner.png"));
        infoBoard.setImage(ResourcesLoader.getImage("infoBanner.png"));
        logoBoard.setImage(ResourcesLoader.getImage("logo.png"));
        chooseBluePieceImage.setImage(ResourcesLoader.getImage("Pawns _blue.png"));
        chooseGreenPieceImage.setImage(ResourcesLoader.getImage("Pawns _gree.png"));
        chooseRedPieceImage.setImage(ResourcesLoader.getImage("Pawns _red.png"));
        chooseYellowPieceImage.setImage(ResourcesLoader.getImage("Pawns _yellow.png"));
        boardImage.setImage(ResourcesLoader.getImage("board.jpg"));
        lockPieceImages();
        startNewGame();
    }

    private void startNewGame() {
        removePiecesFromBoard();
        resetAllTiles();
        unlockPieceImages();
        numberRolledLabel.setText("Game starting...");
        diceIndicatorImage.setImage(ResourcesLoader.getImage("blankdice.png"));
        currentTurnLabel.setText("");
        lockDiceButton();
        lockPieceImages();
    }

    public void playAgain() {
        removePiecesFromBoard();
        resetAllTiles();
        unlockPieceImages();
        numberRolledLabel.setText("Game starting...");
        diceIndicatorImage.setImage(ResourcesLoader.getImage("blankdice.png"));
        currentTurnLabel.setText("");
        lockDiceButton();
    }

    @FXML
    public void onDiceClick() throws IOException {
        playerController.rollDiceAndMove();
    }

    public void localPlayOption(MouseEvent event) {
        unlockPieceImages();
        nameField.setDisable(true);
        nameField.setVisible(false);
        localPlay.setDisable(true);
        localPlay.setVisible(false);
    }

    @FXML
    public void choosePieceImage(MouseEvent event) {
        ImageView chosenImage = (ImageView) event.getSource();

        final Piece bluePiece = new Piece(GameBoard.PLAYER_STARTING_POSITION, "Blue", inGameBluePieceImage);
        final Piece greenPiece = new Piece(GameBoard.PLAYER_STARTING_POSITION, "Green", inGameGreenPieceImage);
        final Piece redPiece = new Piece(GameBoard.PLAYER_STARTING_POSITION, "Red", inGameRedPieceImage);
        final Piece yellowPiece = new Piece(GameBoard.PLAYER_STARTING_POSITION, "Yellow", inGameYellowPieceImage);
        final Piece blackPiece = new Piece(GameBoard.PLAYER_STARTING_POSITION, "Black", inGameBlackImage);

        Player player1 = null;
        Player player2 = null;

//        if (gameMode == GameMode.LOCAL) {
//            if (chosenImage.equals(chooseBluePieceImage)) {
//                player1 = new HumanPlayer(1, bluePiece);
//                player2 = new HumanPlayer(1, redPiece);
//            }
//            else if (chosenImage.equals(chooseGreenPieceImage)) {
//                player1 = new HumanPlayer(1, greenPiece);
//                player2 = new HumanPlayer(1, yellowPiece);
//            }
//            else if (chosenImage.equals(chooseRedPieceImage)) {
//                player1 = new HumanPlayer(1, redPiece);
//                player2 = new HumanPlayer(1, bluePiece);
//            }
//            else if (chosenImage.equals(chooseYellowPieceImage)) {
//                player1 = new HumanPlayer(1, yellowPiece);
//                player2 = new HumanPlayer(1, greenPiece);
//            }
//        }
//        else if (gameMode == GameMode.NETWORK) {
            if (chosenImage.equals(chooseBluePieceImage)) {
                player1 = new HumanPlayer(1, bluePiece);
                player2 = new HumanPlayer(0, blackPiece);
            } else if (chosenImage.equals(chooseGreenPieceImage)) {
                player1 = new HumanPlayer(1, redPiece);
                player2 = new HumanPlayer(0, blackPiece);
            } else if (chosenImage.equals(chooseRedPieceImage)) {
                player1 = new HumanPlayer(1, greenPiece);
                player2 = new HumanPlayer(0, blackPiece);
            } else if (chosenImage.equals(chooseYellowPieceImage)) {
                player1 = new HumanPlayer(1, yellowPiece);
                player2 = new HumanPlayer(0, blackPiece);
            }
//        }

        gameBoard.setPlayer1(player1);
        gameBoard.setPlayer2(player2);
//        playCTR = player2;

        lockPieceImages();
        updateCurrentTurnLabel();
        unlockDiceButton();
    }


    public void unlockDiceButton() {
        rollTheDiceBtn.setDisable(false);
    }

    public void lockDiceButton() {
        rollTheDiceBtn.setDisable(true);
    }

    private void unlockPieceImages() {
        chooseBluePieceImage.setDisable(false);
        chooseGreenPieceImage.setDisable(false);
        chooseRedPieceImage.setDisable(false);
        chooseYellowPieceImage.setDisable(false);
    }

    private void lockPieceImages() {
        chooseBluePieceImage.setDisable(true);
        chooseGreenPieceImage.setDisable(true);
        chooseRedPieceImage.setDisable(true);
        chooseYellowPieceImage.setDisable(true);
    }

    private void removePiecesFromBoard() {
        gameGrid.getChildren().remove(inGameBluePieceImage);
        gameGrid.getChildren().remove(inGameGreenPieceImage);
        gameGrid.getChildren().remove(inGameRedPieceImage);
        gameGrid.getChildren().remove(inGameYellowPieceImage);
    }

    public void movePieceImages(final int destinationRow, final int destinationColumn) {
        ImageView currentPieceImage = gameBoard.getCurrentPlayer().getPlayerPiece().getPieceImage();
        gameGrid.getChildren().remove(currentPieceImage);
        gameGrid.add(currentPieceImage, destinationColumn, destinationRow);
    }

    private void colorTiles() {
        for (Node node : gameGrid.getChildren()) {
            ((Pane) node).setStyle(DEFAULT_TILE_STYLE);
        }
    }

    public void indicateDiceRoll(int diceRoll) {
        switch (diceRoll) {
            case 1:
                diceIndicatorImage.setImage(ResourcesLoader.getImage("dice-six-faces-one.png"));
                break;
            case 2:
                diceIndicatorImage.setImage(ResourcesLoader.getImage("dice-six-faces-two.png"));
                break;
            case 3:
                diceIndicatorImage.setImage(ResourcesLoader.getImage("dice-six-faces-three.png"));
                break;
            case 4:
                diceIndicatorImage.setImage(ResourcesLoader.getImage("dice-six-faces-four.png"));
                break;
            case 5:
                diceIndicatorImage.setImage(ResourcesLoader.getImage("dice-six-faces-five.png"));
                break;
            case 6:
                diceIndicatorImage.setImage(ResourcesLoader.getImage("dice-six-faces-six.png"));
                break;
        }

        numberRolledLabel.setText(gameBoard.getCurrentPlayer().getPlayerPiece().getColor() + " rolled: " + diceRoll);
    }

    public void updateCurrentTurnLabel() {
        currentTurnLabel.setText(gameBoard.getCurrentPlayer().getPlayerPiece().getColor().toUpperCase());
    }

    public void resetAllTiles() {
        for (Node child : gameGrid.getChildren()) {
            if (child instanceof Pane) {
                child.setStyle(DEFAULT_TILE_STYLE);
            }
        }
    }

    public void setCellStyle(Pane clicked, String style) {
        clicked.setStyle(style);
    }

    public Node getNodeByRowColumnIndex(final int row, final int column) {
        Node result = null;
        ObservableList<Node> childrens = gameGrid.getChildren();

        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    public void gameOver() {
        Stage gameOverStage = new Stage();
        Player winner = gameBoard.getCurrentPlayer();
        Parent root = null;
        PlayAgainOrExitAlert playAgainOrExit = null;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlayAgainOrExitPanel.fxml"));
            root = fxmlLoader.load();
            playAgainOrExit = fxmlLoader.<PlayAgainOrExitAlert>getController();
            playAgainOrExit.setWinnerName(winner.getPlayerPiece().getColor().toUpperCase());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        gameOverStage.setTitle("Game over");
        assert root != null;
        gameOverStage.setScene(new Scene(root));
        gameOverStage.show();
        gameOverStage.setAlwaysOnTop(true);

        gameOverStage.setOnCloseRequest(ev -> {
            playAgainOrNot();
        });
    }

    private void playAgainOrNot() {
        if (PlayAgainOrExitAlert.isRematch) {
            playAgain();
        } else {
            System.exit(0);
        }
    }

    public void reportError(String exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(exception);
        alert.showAndWait();
    }

    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    @Override
    public void run() {

    }
}
