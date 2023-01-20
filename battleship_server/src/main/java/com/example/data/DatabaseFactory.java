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
    public static int addUser(String nickname){
        var insertSql = "INSERT INTO users(name)"
                + " VALUES('" + nickname +"')";
        try {
            var affectedRows = statement.executeUpdate(insertSql,  Statement.RETURN_GENERATED_KEYS);

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return -1;
    }

    public static void addGame(int winner, int loser){
        Timestamp tsmp = new Timestamp(System.currentTimeMillis());
        var insertSql = "INSERT INTO games(winner, loser, game_date) VALUES("+winner+","+loser+",'"+tsmp+"')";
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
