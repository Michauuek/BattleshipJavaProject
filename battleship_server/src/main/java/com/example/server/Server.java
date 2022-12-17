package com.example.server;


import lombok.Data;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Data
public class Server {

    private final static int SERVER_PORT= 8081;
    private ServerSocket serverSocket;
    private Player firstPlayer;
    private Player secondPlayer;
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public void startServer()  {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server has started and listening to port: " + SERVER_PORT);
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error creating server");
        }

        while(true) {
            try {
                firstPlayer = new Player(serverSocket.accept());
                new PrintWriter(firstPlayer.getSocket().getOutputStream()).println("Player 1");
                System.out.println("Player 1 connected" + firstPlayer.getSocket().getLocalSocketAddress());

                new PrintWriter(firstPlayer.getSocket().getOutputStream()).println("Waiting for player 2");

                secondPlayer = new Player(serverSocket.accept());
                new PrintWriter(secondPlayer.getSocket().getOutputStream()).println("Player 2");
                System.out.println("Player 2 connected" + secondPlayer.getSocket().getLocalSocketAddress());

                System.out.println("Server socket" + serverSocket.toString());

                executor.execute(new GameSession(firstPlayer, secondPlayer));
                System.out.println("Started new game");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
