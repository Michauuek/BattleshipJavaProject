package com.example.model;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class Ship {
    private List<Coordinate> positions;
    private final int length;
    private boolean isPlaced;


    private Ship(int length) {
        this.length = length;
        this.positions = new ArrayList<>();
    }

    private Ship(int length, ArrayList<Coordinate> positions) {
        this.length = length;
        this.positions = positions;
    }

    /*public static Ship oneSquaresShip(ArrayList<Coordinate> positions) {
        return positions.size() == 1 ? new Ship(1, positions) : new Ship(1);
    }
    public static Ship twoSquaresShip(ArrayList<Coordinate> positions) {
        return positions.size() == 2 ? new Ship(2, positions) : new Ship(2);
    }
    public static Ship threeSquaresShip(ArrayList<Coordinate> positions) {
        return positions.size() == 3 ? new Ship(3, positions) : new Ship(3);
    }
    public static Ship fourSquaresShip(ArrayList<Coordinate> positions) {
        return positions.size() == 4 ? new Ship(4, positions) : new Ship(4);
    }
    public static Ship fiveSquaresShip(ArrayList<Coordinate> positions) {
        return positions.size() == 5 ? new Ship(5, positions) : new Ship(5);
    }*/
}
