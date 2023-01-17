package com.example.battleship_client.controller;

import com.example.battleship_client.model.BoardSquare;
import com.example.battleship_client.model.Coordinate;
import com.example.battleship_client.networking.DataReader;
import com.example.battleship_client.networking.DataWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameController implements Initializable {

    @FXML
    private GridPane UserGrid;

    @FXML
    private GridPane EnemyGrid;

    //console
    @FXML
    private TextField tfMessage;
    @FXML
    private Button buttonMessage;
    @FXML
    private ScrollPane spMain;

    @FXML
    private VBox vboxMessages;

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
        createEnemyBoard(EnemyGrid);

        buttonMessage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String message = tfMessage.getText();
                if (!message.isEmpty()) {
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_RIGHT);

                    hbox.setPadding(new Insets(5, 5, 5, 10));
                    Text text = new Text(message);
                    TextFlow textFlow = new TextFlow(text);
                    text.setFill(Color.WHITE);
                    hbox.getChildren().add(textFlow);
                    vboxMessages.getChildren().add(hbox);
                    tfMessage.clear();
                }
            }
        });
    }

    private void createBoard(GridPane grid) {
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                var square = new BoardSquare();
                grid.add(square, i, j);
            }
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
