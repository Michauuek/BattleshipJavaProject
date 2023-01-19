package com.example.data;

import lombok.Data;
import lombok.Getter;

import java.sql.*;
import java.time.LocalDateTime;

public class DatabaseFactory {

    private static Connection connect;
    private static Statement statement;

    public static void connect() {
        try {
            connect = createConnection();
            statement = connect.createStatement();

            createUserTable();
            createGames();
        } catch (Exception e) {
            e.printStackTrace();
        } //close connection
    }
    public static void addUser(String nickname){
        var date = LocalDateTime.now();
        var insertSql = "INSERT INTO users(name)"
                + " VALUES('" + nickname +"')";
        try {
            statement.executeUpdate(insertSql);
        }
        catch(SQLException e){
            System.out.println(e);
        }
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

    private static void close() {
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
