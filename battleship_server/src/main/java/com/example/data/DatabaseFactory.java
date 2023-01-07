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
        } catch (Exception e) {
            e.printStackTrace();
        } //close connection
    }
    public static void addUser(String nickname) throws SQLException {
        var date = LocalDateTime.now();
        var insertSql = "INSERT INTO users(name, game_date)"
                + " VALUES('" + nickname +"', '" + date + "')";
        statement.executeUpdate(insertSql);
    }

    private static void createUserTable() throws SQLException {
        String tableSql = "CREATE TABLE IF NOT EXISTS users"
                + "(id int PRIMARY KEY AUTO_INCREMENT, name varchar(30),"
                + "game_date datetime)";
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
