package ecgm.snakeandladder.snakeandladder;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board extends Rectangle {

    public Board (int x, int y) {

        setWidth(Game.Square_Size);
        setHeight(Game.Square_Size);

        setFill(Color.rgb( 0, 127, 102));
        setStroke(Color.WHITE);
    }
}
