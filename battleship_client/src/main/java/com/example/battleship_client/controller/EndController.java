package com.example.battleship_client.controller;

import com.example.battleship_client.model.Game;
import com.example.battleship_client.networking.DataReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class EndController implements Initializable {
    @FXML
    private Label messageLabel;
    @FXML
    private VBox rankingVbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageLabel.setText(GameController.getWinner().toUpperCase());
        addLastGames(List.of());
    }


    public void addLastGames(List<Game> games) {
        var num = 1;
        for(var game : games){
            var hbox = createHBox();
            var text = createText(num + ". " + game.getWinner() + " vs " + game.getLoser() + "at " + game.getDate());

            TextFlow textFlow = new TextFlow(text);
            hbox.getChildren().add(textFlow);
            rankingVbox.getChildren().add(hbox);
            num++;
        }
    }

    private HBox createHBox() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);

        hbox.setPadding(new Insets(5, 5, 5, 10));
        return hbox;
    }

    private Text createText(String message) {
        Text text = new Text(message);
        text.setFill(Color.WHITE);
        text.setFont(new Font("Century Gothic", 18));
        return text;
    }
}
