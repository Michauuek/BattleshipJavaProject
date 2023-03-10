package com.example.server;


import com.example.data.DatabaseFactory;
import com.example.data.GameRepository;
import com.example.data.UserRepository;
import com.example.model.BoardModel;
import com.example.model.Coordinate;
import com.example.model.Message;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;


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
            execute(command, args, sender, receiver);
        } catch (Exception e) {
            sender.write(Message.newMessage("[Server] error: " + e.getMessage()));
            receiver.write(Message.newMessage("[Server] error: " + e.getMessage()));
        }
    }

    void handleShoot(Coordinate coord, Player sender, Player receiver) {
        System.out.println("Player " + sender.getName() + " shot at " + coord);

        // check if it is sender turn
        if (firstPlayerTurn != sender.isFirstPlayer()) {
            throw new RuntimeException("It is not your turn!");
        }
        if(sender.getHistory()[coord.getRow()][coord.getColumn()]){
            throw new RuntimeException("This field has already been shot!");
        }
        sender.getHistory()[coord.getRow()][coord.getColumn()] = true;
        // check if the coordinate is valid
        boolean didHit = false;
        for(var ship : receiver.getBoard().board){
            for(var field : ship){
                if(field.equals(coord)){
                    didHit = true;
                    field.setHit(true);
                }
            }
        }

        // send the shoot to the receiver
        boolean finalDidHit = didHit;

        sender.write(new Message("hit", new HashMap<>() {{
            put("your", "true");
            put("coords", new Gson().toJson(coord));
            put("didHit", String.valueOf(finalDidHit));
        }}));

        receiver.write(new Message("hit", new HashMap<>() {{
            put("your", "false");
            put("coords", new Gson().toJson(coord));
            put("didHit", String.valueOf(finalDidHit));
        }}));

        broadcast(Message.newMessage("[Server] Player " + sender.getName() + " shot at " + coord + " and " + (didHit ? "hit!" : "missed!")));

        if(!finalDidHit) {
            firstPlayerTurn = !firstPlayerTurn;
        }
        else{
            isEnd(coord, sender,receiver);
        }

        firstPlayer.write(new Message("turn", new HashMap<>() {{
            put("your", String.valueOf(firstPlayerTurn));
        }}));
        secondPlayer.write(new Message("turn", new HashMap<>() {{
            put("your", String.valueOf(!firstPlayerTurn));
        }}));
    }

    private void isEnd(Coordinate coord, Player sender, Player receiver){
        for(var ship : receiver.getBoard().board){
            for(var field : ship){
                if(!field.isHit())
                    return;
            }
        }
        GameRepository.addGame(sender.getId(),receiver.getId());

        var lastGames = GameRepository.getLastGames(5);
        String list = "";
        if(lastGames != null) {

            for(var game : lastGames){
                System.out.println(game.getWinner() + " won, " + game.getLoser() + " lost");
            }
            Gson gson = new Gson();
            list = gson.toJson(lastGames);
        }

        String finalList = list;
        sender.write(new Message("end", new HashMap<>() {{
            put("winner", "true");
            put("list", finalList);
        }}));

        receiver.write(new Message("end", new HashMap<>() {{
            put("winner", "false");
            put("list", finalList);
        }}));
        //kill the game...
    }
    void broadcast(Message message) {
        firstPlayer.write(message);
        secondPlayer.write(message);
    }

    private void execute(String command, String[] args, Player sender, Player receiver) throws Exception {
        if(command.equals("shoot")) {
            Coordinate cord;

            if(args.length == 1){
                cord = Coordinate.fromString(args[0]);
            }
            else if(args.length == 2){
                cord = Coordinate.fromArgs(args[0].charAt(0), args[1].charAt(0));
            } else{
                throw new Exception("Invalid coordinate");
            }

            // do sth with cord
            // send shoot message to both players

            this.handleShoot(cord, sender, receiver);
            return;
        }
        if(command.equals("help")) {
            broadcast(Message.newMessage(helpMessage));
            return;
        }
        if(command.equals("surrender")) {
            broadcast(Message.newMessage("[Server] Player " + sender.getName() + " surrendered!"));
            return;
        }
        throw new Exception("Unknown command");
    }

    String helpMessage = "Available commands:\n" +
                         "/shoot <row> <col> - shoot at the given coordinate\n" +
                         "/help - show this message\n" +
                         "/surrender - surrender the game";

    private void handleCommands(Message message, Player sender, Player receiver) throws IOException {
        if(message.content.equals("greeting")) {
            sender.setName(message.adds.get("name"));
            sender.id = UserRepository.addUser(sender.getName());

            Gson gson = new Gson();
            BoardModel board = gson.fromJson(message.adds.get("board"), BoardModel.class);
            System.out.println(board);
            sender.setBoard(board);

            broadcast(Message.newMessage("Player " + sender.getName() + " joined the game!"));


            if(receiver.getName() == null) {
                sender.write(new Message("turn", new HashMap<>() {{
                    put("your", "true");
                }}));
            }
            else{
                sender.write(new Message("turn", new HashMap<>() {{
                    put("your", "false");
                }}));
            }
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
            while (!firstPlayer.getSocket().isClosed() || !secondPlayer.getSocket().isClosed()) {
                var p1 = firstPlayer.readMessage();
                if(p1 != null) {
                    System.out.println("[DEBUG] MESSAGE FROM " + firstPlayer.getName() + ": " + p1);
                    if(p1.content.equals("message")) {
                        broadcast(Message.newMessage("[" + firstPlayer.getName() + "] " + p1.adds.get("message")));
                    }
                    handleCommands(p1, firstPlayer, secondPlayer);
                }

                var p2 = secondPlayer.readMessage();
                if(p2 != null) {
                    System.out.println("[DEBUG] MESSAGE FROM " + secondPlayer.getName() + ": " + p2);
                    if(p2.content.equals("message")) {
                        broadcast(Message.newMessage("[" + secondPlayer.getName() + "] " + p2.adds.get("message")));
                    }
                    handleCommands(p2, secondPlayer, firstPlayer);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally{
            if(firstPlayer.getSocket().isClosed()){
                secondPlayer.closeConnection();
            } else {
                firstPlayer.closeConnection();
            }
        }
    }
}
