package com.example.battleship_client.controller;

import com.example.battleship_client.model.Coordinate;
import com.example.battleship_client.model.Ship;

import java.util.ArrayList;
import java.util.List;

public class GlobalGameState {
    public static String serverAddress = "";
    public static String name;
    public static List<Ship> initialShips = new ArrayList<>() {{
        add(Ship.fiveHolesShip(0,0));
        add(Ship.fourHolesShip(6, 9));
        add(Ship.fourHolesShip(4, 4));
        add(Ship.threeHolesShip(0, 9));
        add(Ship.threeHolesShip(2, 6));
        add(Ship.threeHolesShip(7, 7));
        add(Ship.twoHolesShip(8, 0));
        add(Ship.twoHolesShip(8, 2));
        add(Ship.twoHolesShip(4, 2));
        add(Ship.twoHolesShip(0, 2));
    }};

    public static Ship selecedShip;
}
