package com.example.battleship_client.controller;

import com.example.battleship_client.model.*;
import com.example.battleship_client.networking.DataReader;
import com.example.battleship_client.networking.DataWriter;
import com.google.gson.Gson;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameController implements Initializable {
    @FXML
    private GridPane UserGrid;
    @FXML
    private GridPane EnemyGrid;

    //Console elements
    @FXML
    private TextField tfMessage;
    @FXML
    private Button buttonMessage;
    @FXML
    private ScrollPane spMain;
    @FXML
    private VBox vboxMessages;

    private DataWriter dataWriter;
    private DataReader dataReader;
    private ConsoleController consoleController;

    private ConcurrentLinkedQueue<String> messeges = new ConcurrentLinkedQueue<>();

    private Thread addMessageThread = new Thread(() -> {
        while (true) {
            var msg = dataReader.readMessage();
            if (msg != null) {
                if(msg.content.equals("message")){
                    messeges.add(msg.adds.get("message"));
                }
            }
        }
    });

    public GameController() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), 8082);

            dataWriter = new DataWriter(socket);
            dataReader = new DataReader(socket);

            System.out.println(analyzeBoard());

            // get user name
            var name = GlobalGameState.name;

            Gson gson = new Gson();
            BoardModel boardModel = new BoardModel(GlobalGameState.initialShips);
            String ships = gson.toJson(boardModel, BoardModel.class);
            System.out.println(ships);
            // send user name to server
            var message = new Message("greeting", Map.of("name", name, "board", ships));
            System.out.println(message.toJson());
            DataWriter.sendData(message);

            // start thread to read messages
            addMessageThread.start();
        } catch (IOException e){
            System.err.print("Server not found");
        }
    }
/*
* Ugly as hell :///
 */
    boolean analyzeBoard(){
        var ships = GlobalGameState.initialShips;
//        for (int i = 0; i < ships.size(); i++){
//            for (int j = 0; j < ships.get(i).getBoardCoordinates().size(); j++){
//                for(int k = i+1; k < ships.size(); k++){
//                    for(int m = 0; m < ships.get(k).getBoardCoordinates().size(); m++){
//                        if(ships.get(i).getBoardCoordinates().get(j)
//                                .isNearby(ships.get(k).getBoardCoordinates().get(m))){
//                            return false;
//                        }
//
//                    }
//                }
//            }
//        }

        for (int i = 0; i < ships.size(); i++){
            for(int k = i+1; k < ships.size(); k++){
                    if(ships.get(i).isNearby(ships.get(k)))
                        return false;
                    }
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGrid(UserGrid);
        initializeGrid(EnemyGrid);
        createBoard(UserGrid);
        createEnemyBoard(EnemyGrid);

        consoleController = new ConsoleController(
                tfMessage,
                buttonMessage,
                spMain,
                vboxMessages,
                UserGrid
        );

        buttonMessage.setOnAction(event -> {
            String message = tfMessage.getText();
            if (!message.isEmpty()) {
                DataWriter.sendData(Message.newMessage(message));
            }
        });

        tfMessage.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String message = tfMessage.getText();
                if (!message.isEmpty()) {
                    DataWriter.sendData(Message.newMessage(message));
                }
            }
        });

        var animator = new AnimationTimer() {
            final long NANOSECONDS_IN_SECOND = 1000000000;
            long startTime = System.nanoTime();
            @Override
            public void handle(long arg0) {
                long currentTime = System.nanoTime();
                if (NANOSECONDS_IN_SECOND/10 <= (currentTime - startTime)) {
                    update();
                    startTime = currentTime;
                }
            }
        };
        animator.start();
    }

    public void update(){
        var msg = messeges.poll();
        if (msg != null) {
            consoleController.addNewMessage(msg);
        }
    }


    /***
     * Function to create board
     * And add ships from previous screen
     * @param grid
     */
    private void createBoard(GridPane grid) {
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                var square = new BoardSquare();
                grid.add(square, i, j);
            }
        }
        for(var ship : GlobalGameState.initialShips){
            ship.addShipGrid(grid);
            ship.disableDragging();
        }
    }

    private void createEnemyBoard(GridPane grid) {
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                var square = new BoardSquare();
                grid.add(square, i, j);

                square.setOnMouseClicked(mouseEvent -> {
                    square.setFill(Color.GREEN);
                    square.setDisable(true);
                    var ship = new Coordinate(GridPane.getRowIndex(square), GridPane.getColumnIndex(square));
                });
            }
        }
    }

    private void initializeGrid(GridPane grid){
        grid.setHgap(4);
        grid.setVgap(4);
    }
}
