package com.example.server;


import com.example.model.Coordinate;
import com.example.model.Message;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class GameSession implements Runnable {
    private final Player firstPlayer;
    private final Player secondPlayer;

    private boolean firstPlayerTurn = true;

    public GameSession(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    void parseAndExecuteCommand(String message, Player sender, Player receiver) {
        var tokens = message.toLowerCase().strip().replace("/", "").split(" ");

        var command = tokens[0];
        var args = tokens.length > 1 ? Arrays.stream(tokens).skip(1).toArray(String[]::new) : new String[0];

        try {
            Execute(command, args, sender, receiver);
        } catch (Exception e) {
            sender.write(Message.newMessage("[Server] error: " + e.getMessage()));
            receiver.write(Message.newMessage("[Server] error: " + e.getMessage()));
        }
    }

    void handleShoot(Coordinate cord, Player sender, Player receiver) {
        System.out.println("Player " + sender.name + " shot at " + cord);

        // check if it is sender turn
        if (firstPlayerTurn == sender.isFirstPlayer()) {
            throw new RuntimeException("It is not your turn!");
        }

        // check if the coordinate is valid

        // send the shoot to the receiver
        sender.write(new Message("hit", new HashMap<String, String>() {{
            put("your", "true");
            put("cords", new Gson().toJson(cord));
        }}));

        receiver.write(new Message("hit", new HashMap<String, String>() {{
            put("your", "false");
            put("cords", new Gson().toJson(cord));
        }}));
    }

    void Execute(String command, String[] args, Player sender, Player receiver) throws Exception
    {
        if(command.equals("shoot")) {
            var cord = new Coordinate(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

            // do sth with cord
            // send shoot message to both players
            this.handleShoot(cord, sender, receiver);
        }
    }

    void handleCommands(Message message, Player sender, Player receiver) throws IOException {
        if(message.content.equals("greetings")) {
            sender.name = message.adds.get("name");
            sender.write(Message.newMessage("[Server] Player " + sender.name + " connected"));
            receiver.write(Message.newMessage("[Server] Player " + sender.name + " connected"));
        }
        if(message.content.equals("message")) {
            var msg = message.adds.get("message");

            if(msg.startsWith("/")) {
                parseAndExecuteCommand(msg, sender, receiver);
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                var p1 = firstPlayer.readMessage();
                if(p1 != null) {
                    if(p1.content.equals("message")) {
                        System.out.println("Player1: " + p1);
                        secondPlayer.write(Message.newMessage("[" + firstPlayer.getName() + "] " + p1.adds.get("message")));
                        firstPlayer.write(Message.newMessage("[" + firstPlayer.getName() + "] " + p1.adds.get("message")));
                    }
                    handleCommands(p1, firstPlayer, secondPlayer);
                }

                var p2 = secondPlayer.readMessage();
                if(p2 != null) {
                    if(p2.content.equals("message")) {
                        System.out.println("Player2: " + p2);
                        secondPlayer.write(Message.newMessage("[" + secondPlayer.getName() + "] " + p2.adds.get("message")));
                        firstPlayer.write(Message.newMessage("[" + secondPlayer.getName() + "] " + p2.adds.get("message")));

                    }
                    handleCommands(p2, secondPlayer, firstPlayer);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
