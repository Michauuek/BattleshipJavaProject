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

    private double startX;
    private double startY;

    private BoardSquare ship;

    private int currentShipColumn = 0;
    private int currentShipRow = 0;
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

                /*square.setOnMouseEntered(event -> {
                    Node node = (Node) event.getSource();
                    currentShipColumn = GridPane.getColumnIndex(node);
                    currentShipRow = GridPane.getRowIndex(node);

                    System.out.println("Column " + currentShipColumn + " Row "+ currentShipRow);
                });*/
            }
        }
    }

    //TODO add all 5 types of ships
    private void createShips(GridPane grid){
        ship = new BoardSquare();
        ship.setFill(Color.GREEN);
        //makeDraggable(shipOne);
        Ships.getChildren().add(ship);

        var drag = new DragController(ship, true);

/*        ship.setOnMouseReleased(event -> {
            UserGrid.add(ship, currentShipColumn, currentShipRow);
            ship.setDisable(true);
        });*/

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
        });
    }

    /*private void makeDraggable(Node node){
        node.setOnMousePressed(e -> {
            startX = e.getSceneX() - node.getTranslateX();
            startY = e.getSceneY() - node.getTranslateY();
        });

        node.setOnMouseDragged(e -> {
            node.setTranslateX(e.getSceneX() - startX);
            node.setTranslateY(e.getSceneY() - startY);
        });
    }*/

    private void initializeGrid(GridPane grid){
        grid.setHgap(4);
        grid.setVgap(4);

        var ship = new Ship(5, 2, 2);
        ship.addToGrid(grid);

        /*var ship5 = Ship.createShip(5, new Coordinate(1,1));
        addToBoard(ship5, Color.BLACK, grid);

        var ship4 = Ship.createShip(4, new Coordinate(2,3));
        addToBoard(ship4, Color.RED, grid);

        var ship3 = Ship.createShip(3, new Coordinate(3,5));
        addToBoard(ship3, Color.BLUEVIOLET, grid);

        var ship2 = Ship.createShip(2, new Coordinate(4,7));
        addToBoard(ship2, Color.YELLOW, grid);

        var ship1 = Ship.createShip(1, new Coordinate(5,9));
        addToBoard(ship1, Color.GREEN, grid);*/
    }

    /*private void addToBoard(Ship ship, Color color, GridPane grid){
        for(var coordinate : ship.getCoordinates()){
            var square = new BoardSquare();
            square.setColor(color);
            grid.add(square, coordinate.getRow(), coordinate.getColumn());

            // Make the ship cell draggable
            square.setOnMouseDragged((MouseEvent event) -> {
                double newX = event.getSceneX() - square.getScene().getWindow().getX() - square.getScene().getX();
                double newY = event.getSceneY() - square.getScene().getWindow().getY() - square.getScene().getY();
                square.setTranslateX(newX - square.getLayoutX());
                square.setTranslateY(newY - square.getLayoutY());
            });
        }

    }*/
}
