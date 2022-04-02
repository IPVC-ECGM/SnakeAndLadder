package ecgm.snakeandladder.snakeandladder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static final int Tile_Size = 80;
    public static final int width = 10;
    public static final int height = 10;

    private Group TileGroup = new Group();

    private Parent createContent() {
        StackPane root = new StackPane();
        root.setPrefSize(width*Tile_Size, (height*Tile_Size)+80); //+80 porque vamos adicionar os botões no fundo
        root.getChildren().addAll(TileGroup);
        return root;
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(createContent());
        stage.setTitle("Snake and Ladder");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}