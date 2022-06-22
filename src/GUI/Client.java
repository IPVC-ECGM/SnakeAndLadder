package GUI;

import Controller.PlayAgainOrExitAlert;
import Controller.PlayerController;
import Controller.ResourcesLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import Module.GameBoard;
import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import static GUI.GameUIController.*;

public class Client extends Application {
    public PlayerController playerController;

    private Thread clientThread;
    boolean myTurn;
    private GameBoard gameBoard;
    private GameUIController gameUI;
    private static final String serverIP = "127.0.0.1";
    private static final int serverPort = 6667;
    static DataInputStream inputData;
    public static DataOutputStream outputData;
    static ObjectInputStream inputObject;
    static ObjectOutputStream outputObject;
    private PlayerController runningGame;
    private String adversaryName;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/GUI/GameUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/GUI/GameUI.css");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("SNAKES AND LADDERS");
        stage.getIcons().add(ResourcesLoader.getImage("snake.png"));
        stage.show();
        connectClient();
    }

    public static void main(String[] args) {
        try{
            launch(args);
            System.out.println("Adeus..");
            outputData.writeUTF("#logout");
            System.exit(0);
        } catch(IOException ex){}
    }

    private void jogoTerminado(String text) throws IOException{
        Platform.runLater(() -> {
            backPane.setVisible(false);
        });
    }

    private void connectClient() throws IOException {
        Socket socket = new Socket(serverIP, serverPort);

        inputData = new DataInputStream(socket.getInputStream());
        outputData = new DataOutputStream(socket.getOutputStream());
        outputObject = new ObjectOutputStream(socket.getOutputStream());
        inputObject = new ObjectInputStream(socket.getInputStream());

        gameBoard = new GameBoard();
        gameUI = new GameUIController();
        runningGame = new PlayerController(gameUI, gameBoard);
        String serverPlayerName = inputData.readUTF();

        // Thread que serve para o cliente envia mensagens para o servidor -- TAKE ACTION LOCAL SEND
        Thread sendMessage;
        sendMessage = new Thread(() -> {
            nameFieldStatic.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode().equals(KeyCode.ENTER)) {
                        try {
//                            runningGame = new PlayerController(gameUI, gameBoard);
                            System.out.println("Name" + nameFieldStatic);
                            outputData.writeUTF("#name-" + nameFieldStatic.getText());

                            Platform.runLater(() -> {
                                GameUIController.playerNameStatic.setText(nameFieldStatic.getText());
                                GameUIController.adversaryNameStatic.setText(adversaryName);
                                localPlayStatic.setDisable(true);
                                localPlayStatic.setVisible(false);
                                nameFieldStatic.setVisible(false);
                            });
                        } catch (IOException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });

            GameUIController.logoutButtonStatic.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        Stage stage = (Stage) GameUIController.logoutButtonStatic.getScene().getWindow();
                        outputData.writeUTF("#logout");
                        stage.close();
                        System.exit(0);
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });
        });

        Thread readMessage;
        readMessage = new Thread(() -> {
            while (true) {

                try {
                    String msg = inputData.readUTF();
                    System.out.println(msg);
                    if(msg.startsWith("#PLAY")){
                        Platform.runLater(() -> {
                            int i = msg.indexOf("-");
                            if (i != -1){
                                String dados = msg.substring(i + 1);
                                System.out.println(dados);
                                gb.getPlayer1().setTurn(0);
                                gb.getPlayer2().setTurn(1);
                                playCTR.move(Integer.parseInt(dados));
                            }
                            gb.getPlayer1().setTurn(1);
                            gb.getPlayer2().setTurn(0);
                            rollTheDiceBtnStatic.setVisible(true);
                            rollTheDiceBtnStatic.setDisable(false);
                        });
                    }
                    else if(msg.startsWith("#roomfull")){
                        System.out.println("Cant play because the room is full");
                        Platform.runLater(() -> {
                            Stage stage = (Stage) nameFieldStatic.getScene().getWindow();
                            stage.close();
                            System.exit(0);
                        });

                    }
                    else if(msg.startsWith("#name")){
                        // #nome-nomeJogador ou #nome-nomeJogador-pronto-vez
                        String[] msgSplit = msg.split("-");
                        String player = adversaryName = msgSplit[1];
                        Boolean ready = false;
                        // verifica se o jogo se encontra pronto para começar
                        if(msgSplit.length == 4 && msgSplit[2].equals("ready")) ready = true;
                        String newMsg = "Player " + player + " entered...";
                        System.out.println(newMsg);
                        Platform.runLater(() -> {
                            GameUIController.chooseBluePieceImageStatic.setDisable(false);
                            GameUIController.chooseGreenPieceImageStatic.setDisable(false);
                            GameUIController.chooseRedPieceImageStatic.setDisable(false);
                            GameUIController.chooseYellowPieceImageStatic.setDisable(false);
                            localPlayStatic.setDisable(true);
                            localPlayStatic.setVisible(false);
                            GameUIController.playerNameStatic.setText(nameFieldStatic.getText());
                            GameUIController.adversaryNameStatic.setText(player);
                        });
                        if(ready){
                            boolean vez = Boolean.parseBoolean(msgSplit[3]);
                            Platform.runLater(() -> {
                                GameUIController.chooseBluePieceImageStatic.setDisable(false);
                                GameUIController.chooseGreenPieceImageStatic.setDisable(false);
                                GameUIController.chooseRedPieceImageStatic.setDisable(false);
                                GameUIController.chooseYellowPieceImageStatic.setDisable(false);
                                localPlayStatic.setDisable(true);
                                localPlayStatic.setVisible(false);
                                GameUIController.playerNameStatic.setText(nameFieldStatic.getText());
                                GameUIController.adversaryNameStatic.setText(player);
//                                GameUIController.backPane.setVisible(true);
                                nameFieldStatic.setVisible(false);
                            });

                        }
                    }
                    else if(msg.startsWith("#ready")){
                        // #pronto-nomeJogador-vez
                        String[] msgSplit = msg.split("-");
                        adversaryName = msgSplit[1];
//                        boolean vez = Boolean.parseBoolean(msgSplit[2]);
                        Platform.runLater(() -> {
                            GameUIController.chooseBluePieceImageStatic.setDisable(false);
                            GameUIController.chooseGreenPieceImageStatic.setDisable(false);
                            GameUIController.chooseRedPieceImageStatic.setDisable(false);
                            GameUIController.chooseYellowPieceImageStatic.setDisable(false);
                            GameUIController.playerNameStatic.setText(nameFieldStatic.getText());
                            GameUIController.adversaryNameStatic.setText(adversaryName);
                        });
                    }
                    else if(msg.startsWith("#wait")){
                        // #espera-nada ou #espera-desistencia
                        String[] msgSplit = msg.split("-");
                        Platform.runLater(() -> {
                            GameUIController.playerNameStatic.setText(nameFieldStatic.getText());
                            GameUIController.adversaryNameStatic.setText(adversaryName);
//                            GameUIController.backPane.setVisible(false);
                            nameFieldStatic.setVisible(false);
                        });
                    }
                    else if (msg.contains("#logout")) {
                            // #logout-nomeAdversario
                            String[] msgSplit = msg.split("-");
                            String mensagem = "Player " + msgSplit[1] + " left the room!\n";
                            GameUIController.infoLabelStatic.setText(mensagem);
                    }
                    else if (msg.contains("#PLAYER1")) {
                        myTurn = true;
                        rollTheDiceBtnStatic.setDisable(false);
                        rollTheDiceBtnStatic.setVisible(true);
                        System.out.println("PLAYER1");
                    }
                    else if (msg.contains("#PLAYER2")) {
                        myTurn = false;
                        rollTheDiceBtnStatic.setDisable(true);
                        rollTheDiceBtnStatic.setVisible(false);
                        System.out.println("PLAYER2");
                    }
                } catch (IOException e) {

                }
            }
        });
        readMessage.start();
        sendMessage.start();

    }
}