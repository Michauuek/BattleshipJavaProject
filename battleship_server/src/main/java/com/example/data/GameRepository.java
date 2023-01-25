package com.example.data;

import com.example.model.Game;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.example.data.DatabaseFactory.statement;

public class GameRepository {

    public static void addGame(int winner, int loser){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        var insertSql = "INSERT INTO games(winner, loser, game_date) VALUES("+winner+","+loser+",'"+timestamp+"')";
        try {
            statement.executeUpdate(insertSql);
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }

    public static List<Game> getLastGames(int amount){
        var games = new ArrayList<Game>();
        var selectSql = "SELECT * FROM games ORDER BY game_date DESC LIMIT "+amount;
        try {
            var result =  statement.executeQuery(selectSql);
            while (result.next()) {
                var i = result.getInt("id");
                var winner = result.getString("winner");
//                var winnerName = "dfdf";
                var winnerName = UserRepository.getUsernameById(Integer.parseInt(winner));

                var loser = result.getString("loser");
//                var loserName = "fgfghjgfd";
                var loserName = UserRepository.getUsernameById(Integer.parseInt(loser));

                var date = result.getDate("game_date");

                var game = new Game(i, winnerName, loserName, date);

                games.add(game);
            }

            return games;
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }
}
