package com.example.game;

public class Game implements Runnable{

    Player player1;
    Player player2;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public void run() {

    }
}
