package com.example.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.data.DatabaseFactory.statement;

public class UserRepository {

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
}