package com.example.server;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameSession implements Runnable {

    public static int PLAYER1 = 1;
    public static int PLAYER2 = 2;

    private Socket firstPlayer;
    private Socket secondPlayer;


    public GameSession(Socket firstPlayer, Socket secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    @Override
    public void run() {
        try {
            BufferedReader fromPlayer1 = new BufferedReader(new InputStreamReader((this.firstPlayer.getInputStream())));
            PrintWriter toPlayer1 = new PrintWriter(this.firstPlayer.getOutputStream(), true);
            BufferedReader fromPlayer2 = new BufferedReader(new InputStreamReader((this.secondPlayer.getInputStream())));
            PrintWriter toPlayer2 = new PrintWriter(this.secondPlayer.getOutputStream(), true);

            while (true) {
                String move = fromPlayer1.readLine();
                System.out.println("Player1 " + move);
                sendMove(toPlayer2, move);

                move = fromPlayer2.readLine();
                System.out.println("Player2 " + move);
                sendMove(toPlayer1, move);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendMove(PrintWriter out, String move) throws IOException {
        out.println(new Gson().toJson(move));
        out.flush();
    }

}
