package com.example.battleship_client.model;


import com.example.battleship_client.controller.GlobalGameState;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;


public class Ship extends Rectangle {
    private List<Rectangle> shipCells;
    private List<Coordinate> boardCoordinates;
    private final int gridX;
    private final int gridY;
    private final int length;
    private final Color color;
    private double initialMouseX;
    private double initialMouseY;
    private double initialShipTranslateX;
    private double initialShipTranslateY;
    private boolean horizontal;
    private boolean selected = false;

    public static Ship fiveHolesShip(int gridX, int gridY){
        return new Ship(5, gridX, gridY, Color.DARKRED);
    }
    public static Ship fourHolesShip(int gridX, int gridY){
        return new Ship(4, gridX, gridY, Color.DARKSALMON);
    }
    public static Ship threeHolesShip(int gridX, int gridY){
        return new Ship(3, gridX, gridY, Color.DARKGREEN);
    }
    public static Ship twoHolesShip(int gridX, int gridY){
        return new Ship(2, gridX, gridY, Color.DARKGREY);
    }
    public static Ship oneHolesShip(int gridX, int gridY){
        return new Ship(1, gridX, gridY, Color.DARKCYAN);
    }

    private Ship(int length, int gridX, int gridY, Color color) {
        this.length = length;
        this.gridX = gridX;
        this.gridY = gridY;
        this.color = color;
        this.horizontal = true;

        addMovement();
    }

    private void addMovement() {
        // Create the list of ship cells
        shipCells = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Rectangle cell = new Rectangle(35, 35, color);
            cell.setArcWidth(8.0);
            cell.setArcHeight(8.0);
            shipCells.add(cell);

            // Make the ship cell draggable
            cell.setOnMousePressed((MouseEvent event) -> {
                initialMouseX = event.getSceneX();
                initialMouseY = event.getSceneY();
                initialShipTranslateX = cell.getTranslateX();
                initialShipTranslateY = cell.getTranslateY();

                //select ship on mouse click
                if(!selected){
                    toggleBorder();
                }
                deselectOtherShips();
            });
            cell.setOnMouseDragged((MouseEvent event) -> {
                double newX = event.getSceneX() - initialMouseX + initialShipTranslateX;
                double newY = event.getSceneY() - initialMouseY + initialShipTranslateY;
                for (Rectangle c : shipCells) {
                    c.setTranslateX(newX);
                    c.setTranslateY(newY);
                }
            });
            cell.setOnMouseReleased((MouseEvent event) -> {
                // Snap the ship to the nearest grid cell
                double snappedX = Math.round(cell.getTranslateX() / 39.0) * 39;
                double snappedY = Math.round(cell.getTranslateY() / 39.0) * 39;

                //update grid coordinates
                var newCoords = updateBoardCoordinate(snappedX, snappedY);
                if(checkCoordValidity(newCoords)){
                    for (Rectangle c : shipCells) {
                        c.setTranslateX(snappedX);
                        c.setTranslateY(snappedY);
                    }
                    boardCoordinates = newCoords;
                }
                else{
                    for (Rectangle c : shipCells) {
                        c.setTranslateX(initialShipTranslateX);
                        c.setTranslateY(initialShipTranslateY);
                    }
                }
            });
        }
    }
    private boolean checkCoordValidity(List<Coordinate> coords){
        for(var field : coords){
            if(field.getRow() > 9 || field.getColumn() > 9 || field.getRow() < 0 || field.getColumn() < 0)
                return false;
        }
        for(var ship : GlobalGameState.initialShips){
            if(ship != this){
                if(ship.isNearbyCoord(coords)){
                    return false;
                }
            }
        }
        return true;
    }

    public void disableDragging() {
        for (Rectangle c : shipCells) {
            c.setOnMousePressed(null);
            c.setOnMouseDragged(null);
            c.setOnMouseReleased(null);
        }
    }
    public void rotate(GridPane grid) {
        horizontal = !horizontal;
        boardCoordinates = new ArrayList<>();
        if (horizontal) {
            for (int i = 0; i < length; i++) {
                shipCells.get(i).setWidth(35);
                shipCells.get(i).setHeight(35);
                grid.getChildren().remove(shipCells.get(i));
                grid.add(shipCells.get(i), gridX + i, gridY);

                //update coordinate
                boardCoordinates.add(new Coordinate(gridX + i, gridY));
            }
        } else {
            for (int i = 0; i < length; i++) {
                shipCells.get(i).setWidth(35);
                shipCells.get(i).setHeight(35);
                grid.getChildren().remove(shipCells.get(i));
                grid.add(shipCells.get(i), gridX, gridY + i);

                //update coordinate
                boardCoordinates.add(new Coordinate(gridX, gridY + i));
            }
        }
    }
    private List<Coordinate> updateBoardCoordinate(double snappedX, double snappedY) {
        var coordinates = new ArrayList<Coordinate>();
        if(horizontal){
            for (int j = 0; j < length; j++) {
                coordinates.add(new Coordinate((int) (gridX + (snappedX /39) + j), (int) (gridY + (snappedY /39))));
            }
        }
        else {
            for (int j = 0; j < length; j++) {
                coordinates.add(new Coordinate((int) (gridX + (snappedX /39)), (int) (gridY + (snappedY /39) + j)));
            }
        }
        return coordinates;
    }
    public void setBoardCoordinates(Coordinate root) {
        boardCoordinates = new ArrayList<>();
        if(horizontal){
            for (int j = 0; j < length; j++) {
                boardCoordinates.add(new Coordinate(root.getRow() + j, root.getColumn()));
            }
        }
        else {
            for (int j = 0; j < length; j++) {
                boardCoordinates.add(new Coordinate(root.getColumn(), root.getRow() + j));
            }
        }
    }
    public void addToGrid(GridPane grid) {
        boardCoordinates = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            grid.add(shipCells.get(i), gridX + i, gridY);
            boardCoordinates.add(new Coordinate(gridX + i, gridY));
        }
    }
    public void addShipGrid(GridPane grid) {
        for (int i = 0; i < length; i++) {
            var newCell = new BoardSquare();
            newCell.setColor((Color) shipCells.get(i).getFill());
            grid.add(newCell, boardCoordinates.get(i).getRow(), boardCoordinates.get(i).getColumn());
        }
    }
    public void toggleBorder(){
        if(!selected){
            shipCells.forEach(rect ->
                    rect.setStyle("-fx-stroke: gold; -fx-stroke-width: 2; -fx-stroke-type: inside"));
            selected = true;
        } else {
            shipCells.forEach(rect ->
                    rect.setStyle("-fx-stroke-width: 0;"));
            selected = false;
        }
    }
    public boolean isNearby(Ship ship){
        return isNearbyCoord(ship.boardCoordinates);
    }
    public boolean isNearbyCoord(List<Coordinate> ship){
        for(var myField : boardCoordinates){
            for(var theirField : ship){
                if(myField.isNearby(theirField))
                    return true;
            }
        }
        return false;
    }

    /**
     * Works but ugly
     * Unselect all other ships
     */
    public void deselectOtherShips(){
        if(GlobalGameState.selecedShip == null){
            GlobalGameState.selecedShip = this;
        }

        if(GlobalGameState.selecedShip != this){
            GlobalGameState.selecedShip.toggleBorder();
            GlobalGameState.selecedShip = this;
        }
    }
    public Boolean isSelected(){
        return selected;
    }
    public List<Coordinate> getBoardCoordinates() {
        return boardCoordinates;
    }
    public int getLength() {
        return length;
    }

    public void deleteFromBoard(GridPane grid){
        for (int i = 0; i < length; i++){
            grid.getChildren().remove(shipCells.get(i));
        }
        shipCells = new ArrayList<>();
        boardCoordinates = new ArrayList<>();
    }

}
