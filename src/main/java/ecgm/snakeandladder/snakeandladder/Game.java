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