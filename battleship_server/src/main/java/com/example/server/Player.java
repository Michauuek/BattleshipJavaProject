package com.example.server;

import com.example.model.Coordinate;
import com.google.gson.Gson;
import lombok.Data;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Getter
public class Player {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Player(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public String read() throws IOException {
        //var ship = new Gson().fromJson(reader.readLine(), Coordinate.class);
        return reader.readLine();
    }
    public void write(String coordinate){
        var converterJson = new Gson().toJson(coordinate);
        this.writer.println(converterJson);
        this.writer.flush();
    }
}
