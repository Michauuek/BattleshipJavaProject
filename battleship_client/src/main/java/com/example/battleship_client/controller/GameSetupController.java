package com.example.battleship_client.controller;

import com.example.battleship_client.model.BoardSquare;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameSetupController implements Initializable {
    @FXML
    private GridPane UserGrid;
    @FXML
    private VBox Ships;
    @FXML
    private Pane TitlePane;
    @FXML
    private Button readyButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoard(UserGrid);
        initializeGrid(UserGrid);
    }

    //TODO allow user add ship by dragging to the board
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
        for(var ship : GlobalGameState.initialShips){
            System.out.println(ship.getBoardCoordinates());
        }

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
            ship.rotate(grid);
        }
    }
}
