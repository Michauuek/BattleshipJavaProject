package com.example.server;

import com.example.model.Coordinate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

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

    public Coordinate read() throws IOException {
        return new Gson().fromJson(reader.readLine(), Coordinate.class);
    }

    public Coordinate readShip() throws IOException {
        //return new Gson().fromJson(reader.readLine(), new TypeToken<List<Coordinate>>(){}.getType());
        return new Gson().fromJson(reader.readLine(), new TypeToken<Coordinate>(){}.getType());
    }

    public String readMessage() throws IOException {
        return reader.readLine();
    }
    public void write(Object coordinate){
        var converterJson = new Gson().toJson(coordinate);
        System.out.println(converterJson);
        this.writer.println(removeQuotesAndUnescape(converterJson));
        System.out.println(converterJson);
        this.writer.flush();
    }

    private String removeQuotesAndUnescape(String uncleanJson) {
        String noQuotes = uncleanJson.replaceAll("^\"|\"$", "");

        return StringEscapeUtils.unescapeJava(noQuotes);
    }
}
