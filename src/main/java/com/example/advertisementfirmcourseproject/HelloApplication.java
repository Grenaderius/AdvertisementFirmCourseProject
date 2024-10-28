package com.example.advertisementfirmcourseproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 330);
        stage.setTitle("Advertisement firm app");
        stage.setScene(scene);
        stage.show();

        HelloController helloController = fxmlLoader.getController();
        helloController.setStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}