package com.example.battleship_client.controller;

import com.example.battleship_client.model.*;
import com.example.battleship_client.networking.DataReader;
import com.example.battleship_client.networking.DataWriter;
import com.google.gson.Gson;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameController implements Initializable {
    @FXML
    private Pane TitleEnemyPane;
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
    private Socket socket;
    private static String winner;
    private ConcurrentLinkedQueue<String> messeges = new ConcurrentLinkedQueue<>();

    private Thread addMessageThread = new Thread(() -> {
        Gson gson = new Gson();
        while (!socket.isClosed()) {
            var msg = dataReader.readMessage();
            if (msg != null) {
                if(msg.content.equals("message")){
                    messeges.add(msg.adds.get("message"));
                }
                else if(msg.content.equals("hit")){
                    boolean your = Boolean.parseBoolean(msg.adds.get("your"));
                    boolean didHit = Boolean.parseBoolean(msg.adds.get("didHit"));
                    Coordinate coord = gson.fromJson(msg.adds.get("coords"), Coordinate.class);

                    if(your){
                        var nodes = getNodesByCoordinate(coord.getColumn(), coord.getRow(), EnemyGrid);
                        var currentNode = nodes
                                .stream()
                                .findFirst()
                                .orElseThrow();
                        if(didHit){
                            drawXOnShip(currentNode, coord, EnemyGrid);;
                        }
                        else {
                            drawDotOnShip(currentNode, coord, EnemyGrid);
                        }
                    }
                    else {
                        var nodes = getNodesByCoordinate(coord.getColumn(), coord.getRow(), UserGrid);
                        var currentNode = nodes
                                .stream()
                                .findFirst()
                                .orElseThrow();
                        if(didHit){
                            drawXOnShip(currentNode, coord, UserGrid);
                        }
                        else{
                            drawDotOnShip(currentNode, coord, UserGrid);
                        }
                    }
                }
                else if(msg.content.equals("end")){
                    String mess = "END OF GAME you " + (msg.adds.get("winner").equals("true") ? "won!" : "lost!");
                    System.out.println(mess);
                    Platform.runLater(() -> {
                        consoleController.addNewMessage(mess);
                        //go to end screen
                        winner = mess;
                        changeScene();
                    });
//                    Gson

                    System.out.println();
                }
                else if(msg.content.equals("turn")){
                    if(msg.adds.get("your").equals("true")){
                        // light green
                        Platform.runLater(() -> {
                            TitleEnemyPane.setStyle("-fx-background-color: #006400");
                            consoleController.addNewMessage("Your Turn!");
                        });
                    }
                    else{
                        //light red
                        Platform.runLater(() -> {
                            TitleEnemyPane.setStyle("-fx-background-color: #8B0000");
                            consoleController.addNewMessage("Not your Turn!");
                        });
                    }
                }
            }
        }
    });
    private void drawXOnShip(BoardSquare shipCell, Coordinate coord, GridPane grid){
        var square = new BoardSquare();
        Platform.runLater(() -> {
            grid.getChildren().remove(shipCell);
            grid.add(square.drawX(shipCell.getFill()), coord.getRow(), coord.getColumn());
        });
    }

    private void drawDotOnShip(BoardSquare shipCell, Coordinate coord, GridPane grid){
        var square = new BoardSquare();
        Platform.runLater(() -> {
            grid.getChildren().remove(shipCell);
            grid.add(square.drawDot(shipCell.getFill()), coord.getRow(), coord.getColumn());
        });
    }

    List<BoardSquare> getNodesByCoordinate(Integer row, Integer column, GridPane grid) {
        List<BoardSquare> matchingNodes = new ArrayList<>();
        for (var node : grid.getChildren()) {
            if(Objects.equals(GridPane.getRowIndex(node), row) && Objects.equals(GridPane.getColumnIndex(node), column)){
                matchingNodes.add((BoardSquare)node);
            }
        }
        return matchingNodes;
    }

    public GameController() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            if(!GlobalGameState.getInstance().serverAddress.isEmpty()){
                socket = new Socket(GlobalGameState.getInstance().serverAddress, 8082);
            } else {
                socket = new Socket(InetAddress.getLocalHost(), 8082);
            }

            dataWriter = new DataWriter(socket);
            dataReader = new DataReader(socket);

            System.out.println(analyzeBoard());

            // get username
            var name = GlobalGameState.getInstance().name;

            Gson gson = new Gson();
            BoardModel boardModel = new BoardModel(GlobalGameState.getInstance().initialShips);
            String ships = gson.toJson(boardModel, BoardModel.class);
            System.out.println(ships);

            // send username to server
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
        var ships = GlobalGameState.getInstance().initialShips;
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
        for(var ship : GlobalGameState.getInstance().initialShips){
            ship.addShipGrid(grid);
            ship.disableDragging();
        }
    }

    private void createEnemyBoard(GridPane grid) {
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                var square = new BoardSquare();
                grid.add(square, i, j);

                int finalI = i;
                int finalJ = j;
                square.setOnMouseClicked(mouseEvent -> {
                    var command = "/shoot " + finalI + " " + finalJ;
                    DataWriter.sendData(Message.newMessage(command));
                    consoleController.addNewMessage(command);
                });
            }
        }
    }

    private void initializeGrid(GridPane grid){
        grid.setHgap(4);
        grid.setVgap(4);
    }

    private void changeScene(){
        try {
            Stage stage = (Stage) buttonMessage.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/EndView.fxml")));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getWinner(){
        return winner;
    }
}
