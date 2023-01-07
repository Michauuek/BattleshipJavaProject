package com.example.battleship_client.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoardSquare extends Rectangle{
    public BoardSquare(){
        this.setWidth(35);
        this.setHeight(35);
        this.setArcWidth(5.0);
        this.setArcHeight(5.0);
        this.setFill(Color.WHITESMOKE);
    }

    public void setColor(Color color){
        this.setFill(color);
    }
}