package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class BoardModel {
     public List<List<Coordinate>> board;

     @Override
     public String toString() {
          return board.toString();
     }
}
