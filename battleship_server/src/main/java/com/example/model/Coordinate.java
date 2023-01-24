package com.example.model;

import lombok.*;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinate {
    private int row;
    private int column;
    private boolean hit = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return row == that.row && column == that.column;
    }

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    public static Coordinate fromString(String s) {
        // a0, b1 format:
        int row1 = s.charAt(0) - '0';
        int column1 = Character.toLowerCase(s.charAt(1)) - 'a';

        System.out.println(column1);

        if(column1 >= 0 && column1 <= 9 && row1 >= 0 && row1 <= 9) {
            return new Coordinate(row1, column1);
        }

        // 00, 11 format:

        int row2 = s.charAt(0) - '0';
        int column2 = s.charAt(1) - '0';

        if(column2 >= 0 && column2 <= 9 && row2 >= 0 && row2 <= 9) {
            return new Coordinate(row2, column2);
        }

        throw new IllegalArgumentException("Invalid coordinate format: " + s);
    }

    public static Coordinate fromArgs(char row, char col) {
        String s = "" + row + col;

        return fromString(s);
    }
    @Override
    public String toString() {
        var columnMappings = "ABCDEFGHIJ";
        var c=  this.getColumn() < columnMappings.length() ? columnMappings.charAt(this.getColumn()) : 'x';
        return String.format("%d%c", this.getRow(), c);
    }
}
