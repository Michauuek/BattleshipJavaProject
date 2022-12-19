package com.example.battleship_client.networking;

import com.example.battleship_client.model.Coordinate;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

public class DataReader implements Runnable {
    private Socket socket;
    private BufferedReader socketReader;


    public DataReader(Socket socket) throws IOException {
        this.socket = socket;
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        readMessage();
        while(true){
            try{
                Coordinate response = new Gson().fromJson(socketReader.readLine(), Coordinate.class);
                System.out.println("Server Response : " + response);
            } catch (IOException exception){
                System.err.println("Error reading data");
            }
        }
    }

    private void readMessage(){
        try {
            System.out.println(socketReader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
