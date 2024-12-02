package com.example.advertisementfirmcourseproject.LoginController;

import Login.CheckDataForLogin;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {
    private final CheckDataForLogin checkDataForLogin = new CheckDataForLogin();
    public Label EntranceLabelId, PassLabelId, LoginLabelId;
    private Stage stage;
    private int counterOfEntries = 0;

    @FXML
    private Label Erors_Label;
    @FXML
    private TextField Login_TF;
    @FXML
    private TextField Pass_TF;

    @FXML
    private Button EntranceButtonId;

    @FXML
    private String getLogin(){
        return Login_TF.getText();
    }

    @FXML
    private String getPass(){
        return Pass_TF.getText();
    }

    @FXML
    protected void onEnterButtonClick() throws InterruptedException {
        Erors_Label.setVisible(true);
        Erors_Label.setText(checkResultForLogin());
    }

    private String checkResultForLogin() throws InterruptedException {

        if(counterOfEntries==5){

            Platform.runLater(() -> {
                Erors_Label.setVisible(true);
                Erors_Label.setText("Ви перевищили кількість спроб входу! Зачекайте 3 хвилини...");
            });

            Login_TF.setVisible(false);
            Pass_TF.setVisible(false);
            EntranceLabelId.setVisible(false);
            PassLabelId.setVisible(false);
            LoginLabelId.setVisible(false);
            EntranceButtonId.setVisible(false);

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.minutes(3), event -> {
                        Login_TF.setVisible(true);
                        Pass_TF.setVisible(true);
                        EntranceLabelId.setVisible(true);
                        PassLabelId.setVisible(true);
                        LoginLabelId.setVisible(true);
                        EntranceButtonId.setVisible(true);
                        counterOfEntries = 0;
                        Erors_Label.setVisible(false);
                    })
            );

            timeline.setCycleCount(1);
            timeline.play();
            return "";
        }else if(checkDataForLogin.checkData(getLogin(), getPass(), stage)){
            return "Вхід виконано вдало!";
        }else counterOfEntries++;
        return "Не правильний логін або пароль";
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}