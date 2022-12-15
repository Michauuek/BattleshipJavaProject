package com.example.logic;



import com.example.model.Message;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

public class MyTimer implements Runnable{
    int time;
    private Session session;
    public MyTimer() {
        time = 0;
    }


    @Override
    public void run() {
        while(true) {
            Message message = new Message();
            message.setFrom("Timer");
            message.setContent("Time since Connected: " + time + 1);
            try {
                session.getBasicRemote().sendObject(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (EncodeException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
