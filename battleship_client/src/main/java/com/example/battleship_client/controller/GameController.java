package com.example.battleship_client.controller;

import com.example.battleship_client.model.BoardSquare;
import com.example.battleship_client.model.Coordinate;
import com.example.battleship_client.networking.DataReader;
import com.example.battleship_client.networking.DataWriter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameController implements Initializable {

    @FXML
    private GridPane UserGrid;

    @FXML
    private GridPane EnemyGrid;

    private DataWriter dataWriter;

    public GameController() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try{
            Socket socket = new Socket(InetAddress.getLocalHost(), 8082);
            dataWriter = new DataWriter(socket);

            //future = executorService.submit(new DataReader(socket));
            Thread thread1 = new Thread(new DataReader(socket));
            thread1.start();
        } catch (IOException e){
            System.err.print("Server not found");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGrid(UserGrid);
        initializeGrid(EnemyGrid);
        createBoard(UserGrid);
        createBoard(EnemyGrid);
    }

    private void createBoard(GridPane grid) {
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                var square = new BoardSquare();
                grid.add(square, i, j);

                square.setOnMouseClicked(mouseEvent -> {
                    square.setFill(Color.GREEN);
                    square.setDisable(true);
                    var ship = new Coordinate(GridPane.getRowIndex(square), GridPane.getColumnIndex(square));
                    System.out.println(ship);
                    dataWriter.sendData(ship);
                });
            }
        }
    }

    private void initializeGrid(GridPane grid){
        grid.setHgap(4);
        grid.setVgap(4);
    }
}
