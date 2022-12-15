package com.example.logic;

import com.example.model.Message;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.TimerTask;

import static com.example.websocket.ChatEndpoint.unicast;

public class MyTimerTask extends TimerTask {
    public Session s;
    int time;
    public MyTimerTask(Session session) {
        this.s = session;
    }
    @Override
    public void run() {
        if(s.isOpen()) {
            Message mess = new Message();
            mess.setFrom("timer");
            time += 5;
            mess.setContent("Time since connected: " + time);

            try {
//                s.getBasicRemote().sendObject(mess);
                unicast(mess,s);
            } catch (IOException e) {

            } catch (EncodeException e) {

            }
        }
        else {
            System.out.println("session is closed");
        }
}}
