package com.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Game {

    //game validation class
    private int[][] player1Board = new int[10][10];
    private int[][] player1GuessBoard = new int[10][10];
    private int[][] player2Board = new int[10][10];
    private int[][] player2GuessBoard = new int[10][10];
}
