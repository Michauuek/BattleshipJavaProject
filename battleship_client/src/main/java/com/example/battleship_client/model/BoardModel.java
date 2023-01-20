package com.example.battleship_client.model;

import java.util.ArrayList;
import java.util.List;

public class BoardModel {
     private List<List<Coordinate>> board;

    public BoardModel(List<Ship> ships) {
        board = new ArrayList<>();
        for (var ship : ships){
            board.add(ship.getBoardCoordinates());
        }
    }
}
