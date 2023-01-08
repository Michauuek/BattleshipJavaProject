package com.example.battleship_client.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoardSquare extends Rectangle{
    public BoardSquare(){
        this.setWidth(35);
        this.setHeight(35);
        this.setArcWidth(8.0);
        this.setArcHeight(8.0);
        //this.setFill(Color.WHITESMOKE);
        this.setFill(Color.color(0.13,0.26,0.38));
    }

    public void setColor(Color color){
        this.setFill(color);
    }
}