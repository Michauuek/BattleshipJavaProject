package com.example.battleship_client.networking;

import com.example.battleship_client.model.Coordinate;
import com.example.battleship_client.model.Message;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;


public class DataReader implements Runnable {
    private Socket socket;
    private BufferedReader socketReader;
    private Thread readerThread;

    private ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<>();

    public DataReader(Socket socket) throws IOException {
        this.socket = socket;
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        readerThread = new Thread(this);

        readerThread.start();
    }

    @Override
    public void run() {
        while(true){
            try{
                var msg = Message.fromJson(socketReader.readLine());

                messages.add(msg);
            } catch (IOException exception){
                System.err.println("Error reading data");
            }
        }
    }

    public Message readMessage(){
        return messages.poll();
    }

    /*@Override
    public Coordinate call() {
        Coordinate ship = null;
        try{
            *//*while(true){
                response = socketReader.readLine();
                System.out.println("Server Response : " + response);
            }*//*
            ship = new Gson().fromJson(socketReader.readLine(), Coordinate.class);
            System.out.println("Server Response : " + ship);

        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Socket read Error");
        }
        return ship;
    }*/
}
