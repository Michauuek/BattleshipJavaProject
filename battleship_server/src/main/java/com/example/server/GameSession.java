package com.example.server;


import com.example.model.Coordinate;

import java.io.IOException;
import java.util.List;


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
            firstPlayer.write(firstPlayer.readMessage());
            secondPlayer.write(secondPlayer.readMessage());

            while (true) {
                List<Coordinate> move = firstPlayer.readShip();
                System.out.println("Player1 " + move);
                secondPlayer.write(move);

                move = secondPlayer.readShip();
                System.out.println("Player2 " + move);
                firstPlayer.write(move);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
