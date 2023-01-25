package com.example.data;

import java.sql.*;
import java.time.LocalDateTime;

public class DatabaseFactory {

    static Connection connect;
    static Statement statement;
    static PreparedStatement getUsrByIdStat;

    public static void connect() {
        try {
            connect = createConnection();
            statement = connect.createStatement();
            getUsrByIdStat = connect.prepareStatement("SELECT name FROM users WHERE id=?");

            createUserTable();
            createGames();
        } catch (Exception e) {
            e.printStackTrace();
        } //close connection
    }

    private static void createUserTable() throws SQLException {
        String tableSql = "CREATE TABLE IF NOT EXISTS users"
                + "(id int PRIMARY KEY AUTO_INCREMENT, "
                + "name varchar(30) NOT NULL)";
        statement.execute(tableSql);
    }
    private static void createGames() throws SQLException {
        String tableSql = "CREATE TABLE IF NOT EXISTS games"
                + "(id int PRIMARY KEY AUTO_INCREMENT, "
                + "winner INT NOT NULL, "
                + "loser INT NOT NULL, "
                + "game_date datetime, "
                + "FOREIGN KEY (winner) REFERENCES users(id), "
                + "FOREIGN KEY (loser) REFERENCES users(id))";
        statement.execute(tableSql);
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/battleship",
                "root", "root");
    }

    public static void close() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
