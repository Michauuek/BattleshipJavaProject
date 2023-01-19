package com.example.battleship_client.controller;

import com.example.battleship_client.model.BoardSquare;
import com.example.battleship_client.model.Coordinate;
import com.example.battleship_client.model.Message;
import com.example.battleship_client.networking.DataReader;
import com.example.battleship_client.networking.DataWriter;
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
        try{
            Socket socket = new Socket(InetAddress.getLocalHost(), 8082);

            dataWriter = new DataWriter(socket);
            dataReader = new DataReader(socket);

            // get user name
            var name = GlobalGameState.name;

            // send user name to server
            DataWriter.sendData(new Message("greetings", Map.of("name", name)));

            // start thread to read messages
            addMessageThread.start();
        } catch (IOException e){
            System.err.print("Server not found");
        }
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
                vboxMessages
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
