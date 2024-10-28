package com.example.advertisementfirmcourseproject;

import Login.CheckDataForLogin;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloController {
    private final CheckDataForLogin checkDataForLogin = new CheckDataForLogin();
    private Stage stage;

    @FXML
    private Label Erors_Label;
    @FXML
    private TextField Login_TF;
    @FXML
    private TextField Pass_TF;

    @FXML
    private String getLogin(){
        return Login_TF.getText();
    }

    @FXML
    private String getPass(){
        return Pass_TF.getText();
    }

    @FXML
    protected void onEnterButtonClick() {
        Erors_Label.setVisible(true);
        Erors_Label.setText(checkDataForLogin.checkData(getLogin(), getPass(), stage));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}