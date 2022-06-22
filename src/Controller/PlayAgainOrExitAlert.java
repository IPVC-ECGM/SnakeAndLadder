package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayAgainOrExitAlert implements Initializable {
    @FXML
    private Label winnerName;

    @FXML
    private Button yesBtn;
    @FXML
    public static Button yesBtnStatic;

    @FXML
    private Button noBtn;
    @FXML
    public static Button noBtnStatic;

    public static boolean isRematch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yesBtnStatic = yesBtn;
        noBtnStatic = noBtn;

        yesBtn.setOnAction(ev -> {
            isRematch = true;
            Stage stage = (Stage) yesBtn.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));
            stage.close();
        });

        noBtn.setOnAction(ev -> {
            isRematch = false;
            Stage stage = (Stage) noBtn.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));
            stage.close();
        });
    }

    public boolean rematchOrNot() {
        return isRematch;
    }

    public void setWinnerName(String winner) {
        winnerName.setText(winner);
    }

    public Label getWinnerName() {
        return winnerName;
    }
}