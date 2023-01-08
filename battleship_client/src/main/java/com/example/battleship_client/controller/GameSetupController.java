package com.example.battleship_client.controller;

import com.example.battleship_client.model.BoardSquare;
import com.example.battleship_client.model.Coordinate;
import com.example.battleship_client.model.Ship;
import com.example.battleship_client.networking.DataWriter;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSetupController implements Initializable {
    @FXML
    private GridPane UserGrid;
    @FXML
    private VBox Ships;

    @FXML
    private Pane TitlePane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoard(UserGrid);
        initializeGrid(UserGrid);
        createShips(UserGrid);
    }

    //TODO allow user add ship by dragging to the board
    private void createBoard(GridPane grid) {
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                var square = new BoardSquare();
                grid.add(square, i, j);

                /*square.setOnMouseClicked(mouseEvent -> {
                    square.setFill(Color.GREEN);
                    square.setDisable(true);
                    var ship = new Coordinate(GridPane.getRowIndex(square), GridPane.getColumnIndex(square));
                    DataWriter.sendData(ship);
                    System.out.println(ship);
                });*/
            }
        }
    }

    //TODO add all 5 types of ships
    private void createShips(GridPane grid){
        /*ship = new BoardSquare();
        ship.setFill(Color.GREEN);
        //makeDraggable(shipOne);
        Ships.getChildren().add(ship);

        var drag = new DragController(ship, true);


        ship.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ship.setX(event.getX());
                ship.setY(event.getY());
            }
        });

        ship.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Set the ship's position to the nearest grid cell when the mouse is released
                int gridSize = 350; // The size of the grid cells in pixels
                double x = Math.round(ship.getX() / gridSize) * gridSize;
                double y = Math.round(ship.getY() / gridSize) * gridSize;
                ship.setX(x);
                ship.setY(y);

                // Add the ship to the GridPane
                grid.add(ship, (int) x / gridSize, (int) y / gridSize);
            }
        });*/
    }

    private void initializeGrid(GridPane grid) {
        grid.setHgap(4);
        grid.setVgap(4);

        var ship = Ship.fiveHolesShip(2,2);
        ship.addToGrid(grid);

        var ship2 = Ship.fourHolesShip(8,1);
        ship2.addToGrid(grid);
        ship2.rotate(grid);

        var ship3 = Ship.threeHolesShip(3,6);
        ship3.addToGrid(grid);
        ship3.rotate(grid);

        var ship4 = Ship.twoHolesShip(7,7);
        ship4.addToGrid(grid);

        var ship5 = Ship.oneHolesShip(9,9);
        ship5.addToGrid(grid);
    }
}
