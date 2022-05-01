package ecgm.snakeandladder.snakeandladder;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Game extends Application {
    //Variáveis do Tabuleiro
    public static final int Square_Size = 80;
    public static final int width = 10;
    public static final int height = 10;
    public static final int but_area = 80;

    //Variável responsevel por guardar cada Quadradinho nesse grupo
    private Group SquareGroup = new Group();

    //Variéveis para os dados
    public int randNum;
    public Label randomResult;

    //Pião/Jogador
    public Circle player1;
    public Circle player2;
    //
    public int playerPos[][] = new int [10] [10];
    //public int ladderPos[][] = new int [6] [3];
    //Posição do Jogador
    public int player_1_position = 1;
    public int player_2_position = 1;
    //Responsável por saber qual é a vez de cada jugador jugar
    public boolean player_1_Turn = true;
    public boolean player_2_Turn = true;
    //PosiçãoX do jpgador - de forma que fique no centro do quadradinho
    public static int player_1_XPos = 40;
    public static int player_1_YPos = 760;
    public static int player_2_XPos = 40;
    public static int player_2_YPos = 760;

    //Responsavel pelo inicio do jogo / caso false ninguem pode rodar o dado
    public boolean gameStart = false;
    public Button gameButton;

    //
    public int posPlay1 = 1;
    public int posPlay2 = 1;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(width* Square_Size, (height* Square_Size)+but_area); //+80 porque vamos adicionar os botões no fundo
        root.getChildren().addAll(SquareGroup);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //Vamos usar a classe board para construir o tabuleiro
                Board board = new Board(Square_Size, Square_Size);
                board.setTranslateX(j * Square_Size);
                board.setTranslateY(i * Square_Size);
                SquareGroup.getChildren().add(board);

                //Obstaculo
                playerPos[i][j] = j * (Square_Size - 40);
            }
        }

        //Obstáculos em matriz
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.println(playerPos[i][j] + "");
            }
            System.out.println();
        }

        player1 = new Circle(40);
//        Image icon1 = new Image("https://raw.githubusercontent.com/IPVC-ECGM/Online-Assets/main/SnakeAndLadder_SRC_FILES/Pawns%20_red.png");
//        ImagePattern imagePattern1 = new ImagePattern(icon1);
//        player1.setFill(imagePattern1);
//        ImageView imageView1 = new ImageView(icon1);
        player1.setId("Player_1");
        //player1.getStyleClass().add("ecgm/snakeandladder/snakeandladder/style.css");
        player1.setTranslateX(player_1_XPos);
        player1.setTranslateY(player_1_YPos);
        //
        player2 = new Circle(40);
//        Image icon2 = new Image("https://raw.githubusercontent.com/IPVC-ECGM/Online-Assets/main/SnakeAndLadder_SRC_FILES/Pawns%20_blue.png");
//        ImagePattern imagePattern2 = new ImagePattern(icon2);
//        player2.setFill(imagePattern2);
//        ImageView imageView2 = new ImageView(icon2);
        player2.setId("Player_2");
        //player2.getStyleClass().add("ecgm/snakeandladder/snakeandladder/style.css");
        player2.setTranslateX(player_2_XPos);
        player2.setTranslateY(player_2_YPos);

        Button button1 = new Button("Player 1");
        button1.setTranslateX(100);
        button1.setTranslateY(820);
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameStart) {
                    if (player_1_Turn) {
                        rollDice();
                        randomResult.setText(String.valueOf(randNum));
                        movePlayer1();
                        playerMovment(player_1_XPos, player_1_YPos, player1);
                        player_1_Turn = false;
                        player_2_Turn = true;
                        player_1_position+=randNum;

                        if (player_1_XPos == 280 && player_1_YPos == 760) {
                            playerMovment(player_1_XPos = 360, player_1_YPos = 600, player1);
                        }
                    }
                }
            }
        });
        //
        Button button2 = new Button("Player 2");
        button2.setTranslateX(650);
        button2.setTranslateY(820);
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameStart) {
                    if (player_2_Turn) {
                        rollDice();
                        randomResult.setText(String.valueOf(randNum));
                        movePlayer2();
                        playerMovment(player_2_XPos, player_2_YPos, player2);
                        player_2_Turn = false;
                        player_1_Turn = true;
                        player_2_position+=randNum;

                        if (player_2_XPos == 280 && player_2_YPos == 760) {
                            playerMovment(player_2_XPos = 360, player_2_YPos = 600, player2);
                        }
                    }
                }
            }
        });

        gameButton = new Button("Start The Game");
        gameButton.setTranslateX(350);
        gameButton.setTranslateY(820);
        gameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameButton.setText("Game Started");
                //
                player_1_XPos = 40;
                player_1_YPos = 760;
                //
                player_2_XPos = 40;
                player_2_YPos = 760;
                //
                player1.setTranslateX(player_1_XPos);
                player1.setTranslateY(player_1_YPos);
                player2.setTranslateX(player_2_XPos);
                player2.setTranslateY(player_2_YPos);
                gameStart = true;
            }
        });

        //Informação do dado
        randomResult = new Label("0");
        randomResult.setTranslateX(300);
        randomResult.setTranslateY(820);

        //Imagem do tabuleiro
//        Image img = new Image("ecgm/snakeandladder/snakeandladder/board.jpg");
//        Image img = new Image("https://media.istockphoto.com/vectors/snakes-and-ladders-vector-id531466314?k=20&m=531466314&s=612x612&w=0&h=u-NCVLkBo8Z9mp_uIbuh08EuNIYkyFOPQH_V-Zbec_0=");
//        ImageView BackGroundImage = new ImageView();
//        BackGroundImage.setImage(img);
//        BackGroundImage.setFitHeight(800);
//        BackGroundImage.setFitWidth(800);

        SquareGroup.getChildren().addAll(player1, player2, button1, button2, gameButton, randomResult);
        return root;
    }

    private void rollDice() {
        randNum = (int) (Math.random()*6+1);
    }

    private void movePlayer1() {
        for (int i = 0; i < randNum; i++) {
            if (posPlay1 % 2 == 1) {
                player_1_XPos+=80;
            }
            if (posPlay1 % 2 == 0) {
                player_1_XPos-=80;
            }
            if (player_1_XPos > 760) {
                player_1_YPos-=80;
                player_1_XPos-=80;
                posPlay1++;
            }
            if (player_1_XPos < 40) {
                player_1_YPos-=80;
                player_1_XPos+=80;
                posPlay1++;
            }
            if (player_1_XPos < 30 || player_1_YPos < 30){
                player_1_XPos = 40;
                player_1_YPos = 40;
                randomResult.setText("Player One Won");
                randomResult.setText("Play Again");
            }
        }
    }

    private void movePlayer2() {
        for (int i = 0; i < randNum; i++) {
            if (posPlay2 % 2 == 1) {
                player_2_XPos+=80;
            }
            if (posPlay2 % 2 == 0) {
                player_2_XPos-=80;
            }
            if (player_2_XPos > 760) {
                player_2_YPos-=80;
                player_2_XPos-=80;
                posPlay2++;
            }
            if (player_2_XPos < 40) {
                player_2_YPos-=80;
                player_2_XPos+=80;
                posPlay2++;
            }
            if (player_2_XPos < 30 || player_2_YPos < 30){
                player_2_XPos = 40;
                player_2_YPos = 40;
                randomResult.setText("Player One Won");
                randomResult.setText("Play Again");
            }
        }
    }

    private void playerMovment(int x, int y, Circle d) {
        TranslateTransition moveAnimation = new TranslateTransition(Duration.millis(1000), d);
        moveAnimation.setToX(x);
        moveAnimation.setToY(y);
        moveAnimation.setAutoReverse(false);
        moveAnimation.play();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(createContent());
//        scene.getStylesheets().add("ecgm/snakeandladder/snakeandladder/style.css");
        stage.setTitle("Snake and Ladder");
        stage.setScene(scene);
        stage.show();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {

            }
        };
    }

    public static void main(String[] args) {
        launch();
    }
}