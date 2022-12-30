package com.example.battleship_client.controller;

import com.example.battleship_client.model.BoardSquare;
import com.example.battleship_client.model.Coordinate;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSetupController implements Initializable {
    @FXML
    private GridPane UserGrid;
    @FXML
    private VBox Ships;

    private double startX;
    private double startY;

    private BoardSquare shipOne;

    private int currentShipColumn = 0;
    private int currentShipRow = 0;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoard(UserGrid);
        initializeGrid(UserGrid);
        createShips();
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
                    System.out.println(ship);
                });*/

                square.setOnMouseEntered(event -> {
                    Node node = (Node) event.getSource();
                    currentShipColumn = GridPane.getColumnIndex(node);
                    currentShipRow = GridPane.getRowIndex(node);

                    System.out.println("Column " + currentShipColumn + " Row "+ currentShipRow);
                });
            }
        }
    }

    //TODO add all 5 types of ships
    private void createShips(){
        shipOne = new BoardSquare();
        shipOne.setFill(Color.GREEN);
        //makeDraggable(shipOne);
        Ships.getChildren().add(shipOne);

        var drag = new DragController(shipOne, true);

        shipOne.setOnMouseReleased(event -> {
            UserGrid.add(shipOne, currentShipColumn, currentShipRow);
            shipOne.setDisable(true);
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
    }
}
