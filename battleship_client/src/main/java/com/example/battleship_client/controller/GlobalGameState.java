package com.example.battleship_client.controller;

import com.example.battleship_client.model.Coordinate;
import com.example.battleship_client.model.Ship;

import java.util.ArrayList;
import java.util.List;

public class GlobalGameState {
    public static String serverAddress = "";
    public static String name;
    public static List<Ship> initialShips = new ArrayList<>() {{
        add(Ship.fiveHolesShip(4,0));
        add(Ship.fourHolesShip(0, 0));
        add(Ship.fourHolesShip(2, 1));
        add(Ship.threeHolesShip(0, 6));
        add(Ship.threeHolesShip(2, 6));
        add(Ship.threeHolesShip(4, 6));
        add(Ship.twoHolesShip(8, 0));
        add(Ship.twoHolesShip(8, 2));
        add(Ship.twoHolesShip(8, 4));
        add(Ship.twoHolesShip(8, 8));
    }};

    public static Ship selecedShip;
}
