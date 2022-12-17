package com.example.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Board {
    private boolean isBoardFull;
    private List<Ship> ships;
    private List<Coordinate> missedShots;

    public Board() {
        ships = new ArrayList<>();
        missedShots = new ArrayList<>();
    }
}
