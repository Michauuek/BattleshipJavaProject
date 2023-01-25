package com.example.battleship_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle("Battleship");

        //stage.initStyle(StageStyle.TRANSPARENT);

        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}