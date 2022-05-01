package ecgm.snakeandladder.snakeandladder;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void swichToMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("src/main/resources/ecgm/snakeandladder/snakeandladder/Menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void swichToGame(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("src/main/resources/ecgm/snakeandladder/snakeandladder/Menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}