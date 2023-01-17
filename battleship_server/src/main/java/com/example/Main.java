package com.example;

import com.example.data.DatabaseFactory;
import com.example.server.Server;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {
        // DatabaseFactory.connect();
        // DatabaseFactory.addUser("Debil");

        Server server = new Server();
        server.startServer();
    }
}