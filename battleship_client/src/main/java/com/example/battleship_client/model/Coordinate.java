package com.example.battleship_client.model;


import java.util.Objects;

public class Coordinate {
    private int row;
    private int column;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return row == that.row && column == that.column;
    }
    public boolean isNearby(Coordinate coordinate){
        if (row == coordinate.row && column == coordinate.column)
            return true;

        if (Math.abs(row - coordinate.row) <= 1 && Math.abs(column - coordinate.column) <= 1)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}