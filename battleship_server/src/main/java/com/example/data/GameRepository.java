package com.example.data;

import java.sql.SQLException;
import java.sql.Timestamp;

import static com.example.data.DatabaseFactory.statement;

public class GameRepository {

    public void addGame(int winner, int loser){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        var insertSql = "INSERT INTO games(winner, loser, game_date) VALUES("+winner+","+loser+",'"+timestamp+"')";
        try {
            statement.executeUpdate(insertSql);
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
}
