package com.example.server;


import lombok.Data;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Data
public class Server {

    private final static int SERVER_PORT= 8081;
    private ServerSocket serverSocket;
    private Socket firstPlayerSocket;
    private Socket secondPlayerSocket;
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public void startServer()  {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Starting server");
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error creating server");
        }

        while(true) {
            try {
                System.out.println("Server has started and listening to port: " + SERVER_PORT);

                firstPlayerSocket = serverSocket.accept();
                new PrintWriter(firstPlayerSocket.getOutputStream()).println("Player 1");
                System.out.println("Player 1 connected" + firstPlayerSocket.getInetAddress().getHostAddress());

                new PrintWriter(firstPlayerSocket.getOutputStream()).println("Waiting for player 2");

                secondPlayerSocket = serverSocket.accept();
                new PrintWriter(secondPlayerSocket.getOutputStream()).println("Player 2");
                System.out.println("Player 2 connected" + secondPlayerSocket.getInetAddress().getHostAddress());

                executor.execute(new GameSession(firstPlayerSocket, secondPlayerSocket));
                System.out.println("Started new game");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
