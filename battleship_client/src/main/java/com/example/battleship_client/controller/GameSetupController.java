package com.example.battleship_client.controller;

import com.example.battleship_client.model.BoardSquare;
import com.example.battleship_client.model.Message;
import com.example.battleship_client.networking.DataWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameSetupController implements Initializable {
    @FXML
    private GridPane UserGrid;
    @FXML
    private Button readyButton;

    //Console elements
    @FXML
    private TextField tfMessage;
    @FXML
    private Button buttonMessage;
    @FXML
    private ScrollPane spMain;
    @FXML
    private VBox vboxMessages;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoard(UserGrid);
        initializeGrid(UserGrid);

        //auto scroll to bottom
        vboxMessages.heightProperty().addListener(observable -> spMain.setVvalue(1D));

        buttonMessage.setOnAction(event -> {
            String message = tfMessage.getText();
            if (!message.isEmpty()) {
                addNewMessage(message);
            }
        });

        tfMessage.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String message = tfMessage.getText();
                if (!message.isEmpty()) {
                    addNewMessage(message);
                }
            }
        });
    }

    private void addNewMessage(String message) {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);

        hbox.setPadding(new Insets(5, 5, 5, 10));
        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        text.setFill(Color.WHITE);
        text.setFont(new Font("Monospaced Regular", 16));
        hbox.getChildren().add(textFlow);
        vboxMessages.getChildren().add(hbox);
        tfMessage.clear();
    }
    private void createBoard(GridPane grid) {
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                var square = new BoardSquare();
                grid.add(square, i, j);
            }
        }
    }
    @FXML
    public void readyClickHandle(ActionEvent event) throws IOException {

        //display ship coordinates
        /*for(var ship : GlobalGameState.initialShips){
            System.out.println(ship.getBoardCoordinates());
        }*/

        System.out.println(GlobalGameState.initialShips.get(0).getBoardCoordinates());

        Stage stage = (Stage) readyButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/GameView.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }


    private void initializeGrid(GridPane grid) {
        grid.setHgap(4);
        grid.setVgap(4);

        //TODO utworzyć losowe dodoawanie statków do grida

        for(var ship : GlobalGameState.initialShips){
            ship.addToGrid(grid);
            //ship.rotate(grid);
        }
    }
}
