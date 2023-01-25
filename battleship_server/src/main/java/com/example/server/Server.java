package com.example.server;


import com.example.data.DatabaseFactory;
import com.example.model.Message;
import lombok.Data;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Data
public class Server {

    private final static int SERVER_PORT= 8082;
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

        while(!serverSocket.isClosed()) {
            try {
                synchronized(serverSocket) {
                    firstPlayer = new Player(serverSocket.accept(), true);
                }

                System.out.println("Player 1 connected" + firstPlayer.getSocket().getLocalSocketAddress());

                firstPlayer.write(Message.newMessage("[Server] Waiting for second player to connect..."));

                secondPlayer = new Player(serverSocket.accept(), false);
                System.out.println("Player 2 connected" + secondPlayer.getSocket().getLocalSocketAddress());
                secondPlayer.write(Message.newMessage("[Server] Connected as player 2 connected"));
                firstPlayer.write(Message.newMessage("[Server] Second player connected"));

                executor.execute(new GameSession(firstPlayer, secondPlayer));
                System.out.println("Started new game");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        DatabaseFactory.close();
    }

}
