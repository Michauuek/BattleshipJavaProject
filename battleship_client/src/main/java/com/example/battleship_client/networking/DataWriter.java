package com.example.battleship_client.networking;

import com.example.battleship_client.model.Coordinate;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import com.google.gson.Gson;

public class DataWriter  {
    private Socket socket;
    private static PrintWriter writer;

    public DataWriter(Socket socket) throws IOException {
        this.socket = socket;
        writer = new PrintWriter(socket.getOutputStream());
    }

    public static void sendData(Coordinate coordinate){
        var converterJson = new Gson().toJson(coordinate);
        writer.println(converterJson);
        writer.flush();
    }
}
