package com.example.battleship_client.networking;

import com.example.battleship_client.model.Coordinate;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.example.battleship_client.model.Message;
import com.google.gson.Gson;

public class DataWriter  {
    private Socket socket;
    private static PrintWriter writer;

    public DataWriter(Socket socket) throws IOException {
        this.socket = socket;
        writer = new PrintWriter(socket.getOutputStream());
    }

    public static void sendData(Message coordinate){
        writer.println(coordinate.toJson());
        writer.flush();
    }
}
