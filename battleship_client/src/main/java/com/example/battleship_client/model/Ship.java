package com.example.battleship_client.model;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Ship  {

    private int length;
    private int posX;
    private int posY;
    private List<Rectangle> squares;



    public Ship(int length) {
        this.length = length;
        /*this.posX = posX;
        this.posY = posY;*/
        this.squares = new ArrayList<>();
        for(int i = 0; i < length; i++){
            this.squares.add(new BoardSquare());
        }
    }

    public List<Rectangle> getSquares() {
        return squares;
    }
}
