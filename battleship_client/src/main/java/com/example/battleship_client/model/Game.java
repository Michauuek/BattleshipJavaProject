package com.example.battleship_client.model;

import java.util.Date;

public class Game {
    private int id;
    private String winner;
    private String loser;
    private String date;

    public Game(int id, String winner, String loser, String date) {
        this.id = id;
        this.winner = winner;
        this.loser = loser;
        this.date = date;
    }

    public String getWinner() {
        return winner;
    }

    public String getLoser() {
        return loser;
    }

    public String getDate() {
        return date;
    }
}
