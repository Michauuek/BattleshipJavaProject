package com.example.game;

import javax.websocket.Session;

public class Player {
    Player opponent;
    String name;
    Session session;

    public Player(Player opponent, String name, Session session) {
        this.opponent = opponent;
        this.name = name;
        this.session = session;
    }

    public Player(String name, Session session) {
        this.name = name;
        this.session = session;
        this.opponent = null;
    }
}
