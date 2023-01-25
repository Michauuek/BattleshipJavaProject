package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Game {
    private int id;
    private String winner;
    private String loser;
    private String date;
}
