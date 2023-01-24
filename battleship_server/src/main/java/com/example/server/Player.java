package com.example.server;

import com.example.model.BoardModel;
import com.example.model.Coordinate;
import com.example.model.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class Player {
    private Socket socket;
    private PrintWriter writer;

    private ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<>();

    private boolean isFirstPlayer;
    @Setter
    @Getter
    private String name = null;

    @Getter
    @Setter
    private BoardModel board;
    private boolean[][] history = new boolean[10][10];

    private Thread readerThread = new Thread(() -> {
        try {
            var reader = new BufferedReader(new InputStreamReader((socket.getInputStream())));

            while (true) {
                try {
                    var msg = Message.fromJson(reader.readLine());
                    messages.add(msg);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });
    public Player(Socket socket, boolean isFirstPlayer) throws IOException {
        this.socket = socket;
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.isFirstPlayer = isFirstPlayer;
        readerThread.start();
    }

    public Message readMessage() throws IOException {
        return messages.poll();
    }

    public void write(Message message) {
        var converterJson = message.toJson();
        System.out.println(converterJson);
        this.writer.println(converterJson);
        this.writer.flush();
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }
}
