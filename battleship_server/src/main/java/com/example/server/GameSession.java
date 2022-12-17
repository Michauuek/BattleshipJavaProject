package com.example.server;


import java.io.IOException;


public class GameSession implements Runnable {
    private Player firstPlayer;
    private Player secondPlayer;

    public GameSession(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String move = firstPlayer.read();
                System.out.println("Player1 " + move);
                secondPlayer.write(move);

                move = secondPlayer.read();
                System.out.println("Player2 " + move);
                firstPlayer.write(move);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
