package com.example.advertisementfirmcourseproject;

import com.example.advertisementfirmcourseproject.LoginController.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdvertisementFirm extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AdvertisementFirm.class.getResource("advertisement-firm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 330);
        stage.setTitle("Advertisement firm app");
        stage.setScene(scene);
        stage.show();

        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}