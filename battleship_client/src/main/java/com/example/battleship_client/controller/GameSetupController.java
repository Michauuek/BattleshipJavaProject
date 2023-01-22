package com.example.battleship_client.controller;

import com.example.battleship_client.model.BoardSquare;
import com.example.battleship_client.model.Coordinate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
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
    private ConsoleController consoleController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoard(UserGrid);
        initializeGrid(UserGrid);

        consoleController = new ConsoleController(
                tfMessage,
                buttonMessage,
                spMain,
                vboxMessages,
                UserGrid
        );

        buttonMessage.setOnAction(event -> {
            String message = tfMessage.getText();
            if (!message.isEmpty()) {
                consoleController.addNewMessage(message, "[" + GlobalGameState.name + "]: ");
            }
        });

        tfMessage.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String message = tfMessage.getText();
                if (!message.isEmpty()) {
                    consoleController.addNewMessage(message, "[" + GlobalGameState.name + "]: ");
                }
            }
        });
    }

    @FXML
    public void readyClickHandle(ActionEvent event) throws IOException {
        System.out.println(GlobalGameState.initialShips.get(0).getBoardCoordinates());

        Stage stage = (Stage) readyButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/GameView.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    private boolean areShipsInValidPosition() {
        HashSet<Coordinate> coordinates = new HashSet<>();

        for (var ship : GlobalGameState.initialShips) {
            for (Coordinate coordinate : ship.getBoardCoordinates()) {
                if (coordinates.contains(coordinate)) {
                    return false;
                }
                coordinates.add(coordinate);
            }
        }
        return true;
    }

    private void randomizeShips() {
        while(true){
            for(var ship : GlobalGameState.initialShips){
                var length = ship.getLength();

                var random = (int)(Math.random() * 10);

                var randomX = (random-length);
                var randomY = (int)(Math.random() * 10);


                ship.setBoardCoordinates(new Coordinate(randomX, randomY));
            }
            if(areShipsInValidPosition()){
                break;
            }
        }
    }

    private void createBoard(GridPane grid) {
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                var square = new BoardSquare();
                grid.add(square, i, j);
            }
        }
    }
    private void initializeGrid(GridPane grid) {
        grid.setHgap(4);
        grid.setVgap(4);

        randomizeShips();

        for(var ship : GlobalGameState.initialShips){
            ship.addToGrid(grid);
        }
    }
}
