package com.example.battleship_client.model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class BoardSquare extends Rectangle{
    public BoardSquare(){
        this.setWidth(35);
        this.setHeight(35);
        this.setArcWidth(8.0);
        this.setArcHeight(8.0);
        this.setFill(Color.color(0.13,0.26,0.38));
    }

    public void setColor(Color color){
        this.setFill(color);
    }

    public Group drawDot(){
        Circle dot = new Circle();
        dot.setCenterX(this.getX() + this.getWidth()/2);
        dot.setCenterY(this.getY() + this.getHeight()/2);
        dot.setRadius(3);
        dot.setFill(Color.GOLD);

        return new Group(this, dot);
    }

    public Group drawX() {
        Line diagonal1 = new Line();
        diagonal1.setStartX(this.getX() + 2);
        diagonal1.setStartY(this.getY() + 2);
        diagonal1.setEndX(this.getX() + this.getWidth() -2);
        diagonal1.setEndY(this.getY() + this.getHeight() -2);

        Line diagonal2 = new Line();
        diagonal2.setStartX(this.getX() + this.getWidth() -2);
        diagonal2.setStartY(this.getY() + 2);
        diagonal2.setEndX(this.getX() + 2);
        diagonal2.setEndY(this.getY() + this.getHeight() - 2);

        diagonal1.setStroke(Color.WHITESMOKE);
        diagonal2.setStroke(Color.WHITESMOKE);

        return new Group(this, diagonal1, diagonal2);
    }
}