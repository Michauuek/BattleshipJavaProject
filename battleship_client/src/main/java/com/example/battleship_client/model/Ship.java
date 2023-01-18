package com.example.battleship_client.model;


import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;


public class Ship extends Rectangle {
    private List<Rectangle> shipCells;
    private List<Coordinate> boardCoordinates;
    private int gridX;
    private int gridY;
    private final int length;
    private double initialMouseX;
    private double initialMouseY;
    private double initialShipTranslateX;
    private double initialShipTranslateY;
    private boolean horizontal;


    private Ship(int length, int gridX, int gridY, Color color) {
        this.length = length;
        this.gridX = gridX;
        this.gridY = gridY;
        this.horizontal = true;

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
                for (Rectangle c : shipCells) {
                    c.setTranslateX(snappedX);
                    c.setTranslateY(snappedY);
                }

                //update grid coordinates
                updateBoardCoordinate(snappedX, snappedY);
            });
        }
    }

    public void disableDragging() {
        for (Rectangle c : shipCells) {
            c.setOnMousePressed(null);
            c.setOnMouseDragged(null);
            c.setOnMouseReleased(null);
        }
    }

    public static final Ship fiveHolesShip(int gridX, int gridY){
        return new Ship(5, gridX, gridY, Color.DARKRED);
    }
    public static final Ship fourHolesShip(int gridX, int gridY){
        return new Ship(4, gridX, gridY, Color.DARKSALMON);
    }
    public static final Ship threeHolesShip(int gridX, int gridY){
        return new Ship(3, gridX, gridY, Color.DARKGREEN);
    }
    public static final Ship twoHolesShip(int gridX, int gridY){
        return new Ship(2, gridX, gridY, Color.DARKGREY);
    }
    public static final Ship oneHolesShip(int gridX, int gridY){
        return new Ship(1, gridX, gridY, Color.DARKCYAN);
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
    private void updateBoardCoordinate(double snappedX, double snappedY) {
        boardCoordinates = new ArrayList<>();
        if(horizontal){
            for (int j = 0; j < length; j++) {
                boardCoordinates.add(new Coordinate((int) (this.gridX + (snappedX /39) + j), (int) (this.gridY + (snappedY /39))));
            }
        }
        else {
            for (int j = 0; j < length; j++) {
                boardCoordinates.add(new Coordinate((int) (this.gridX + (snappedX /39)), (int) (this.gridY + (snappedY /39) + j)));
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
            grid.add(shipCells.get(i),  boardCoordinates.get(i).getRow(), boardCoordinates.get(i).getColumn());
        }
    }

    public List<Coordinate> getBoardCoordinates() {
        return boardCoordinates;
    }

}
