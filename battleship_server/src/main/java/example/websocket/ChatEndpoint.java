package com.example.websocket;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.example.game.Player;
import com.example.logic.MyTimerTask;
import com.example.model.Message;

@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {
    private Session session;
    private static final Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();
    private Timer timer = new Timer();
    private static HashMap<String, Player> players = new HashMap<>();


    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {

        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), username);

        Player player = new Player(username,session);
        players.put(username,player);


        Message message = new Message();
        message.setFrom(username);
        message.setContent("Connected!");
        message.setAdditionalContent(new HashMap<>());
        message.getAdditionalContent().put("siema", "siemkA");
        broadcast(message);
        System.out.println("created endpoint " + username);


        timer.scheduleAtFixedRate(new MyTimerTask(session), 5000, 5000);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        message.setFrom(users.get(session.getId()));
        //broadcast(message);
        unicast(message, session);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        chatEndpoints.remove(this);
        String username = getUsername(session.getId());
        players.remove(username);
        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setContent("Disconnected!");

        broadcast(message);
        System.out.println("Session " + users.get(session.getId()) + " disconnected!");
        timer.cancel();

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        System.out.println("Error Thrown: " + throwable.getMessage());
    }

    public static void broadcast(Message message) throws IOException, EncodeException {
        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote()
                            .sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void unicast(Message message, Session session) throws IOException, EncodeException {
        synchronized (session) {
            try {
                session.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUsername(String sessionId){
        return users.get(sessionId);
    }
    //public Session getSession

    public static void getConnectedUsers(){

        String userNames= "";
        for(Map.Entry<String, String> entry: users.entrySet()) {
            System.out.println(entry.getValue());
            userNames.concat(entry.getValue()).concat(" ");
        }

        Message message = new Message();
        message.setContent("Connected Users List");
        message.setFrom("server");
        message.setAdditionalContent(new HashMap<>());
        message.getAdditionalContent().put("User List", userNames);

    }
}
