module ecgm.snakeandladder.snakeandladder {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens ecgm.snakeandladder.snakeandladder to javafx.fxml;
    exports ecgm.snakeandladder.snakeandladder;
}