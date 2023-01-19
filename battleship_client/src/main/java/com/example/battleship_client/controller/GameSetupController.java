package com.example.battleship_client.controller;

import com.example.battleship_client.model.BoardSquare;
import com.example.battleship_client.model.Coordinate;
import com.example.battleship_client.model.Message;
import com.example.battleship_client.networking.DataWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameSetupController implements Initializable {
    @FXML
    private GridPane UserGrid;
    @FXML
    private Button readyButton;

    //Console elements
    @FXML
    private TextField tfMessage;
    @FXML
    private Button buttonMessage;
    @FXML
    private ScrollPane spMain;
    @FXML
    private VBox vboxMessages;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoard(UserGrid);
        initializeGrid(UserGrid);

        //auto scroll to bottom
        vboxMessages.heightProperty().addListener(observable -> spMain.setVvalue(1D));

        buttonMessage.setOnAction(event -> {
            String message = tfMessage.getText();
            if (!message.isEmpty()) {
                addNewMessage(message, "[" + GlobalGameState.name + "]: ");
            }
        });

        tfMessage.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String message = tfMessage.getText();
                if (!message.isEmpty()) {
                    addNewMessage(message, "[" + GlobalGameState.name + "]: ");
                }
            }
        });
    }

    private void addNewMessage(String message, String owner) {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);

        hbox.setPadding(new Insets(5, 5, 5, 10));
        Text text = new Text(owner+message);
        TextFlow textFlow = new TextFlow(text);
        text.setFill(Color.WHITE);
        text.setFont(new Font("Monospaced Regular", 16));
        hbox.getChildren().add(textFlow);
        vboxMessages.getChildren().add(hbox);
        tfMessage.clear();

        handleCommands(message);
    }
    void handleCommands(String msg)  {
        if(msg.startsWith("/")) {
            parseAndExecuteCommand(msg);
        }
    }
    void parseAndExecuteCommand(String message) {
        var tokens = message.toLowerCase().strip().replace("/", "").split(" ");

        var command = tokens[0];
        var args = tokens.length > 1 ? Arrays.stream(tokens).skip(1).toArray(String[]::new) : new String[0];

        try {
            Execute(command, args);
        } catch (Exception e) {
            addNewMessage("Error: " + e.getMessage(), "[System]: ");
        }
    }
    void Execute(String command, String[] args) throws Exception
    {
        switch(command) {
            case "select" -> Select(args);
            case "rotate" -> Rotate(args);
            case "place" -> Place(args);
            case "ready" -> Ready(args);
            case "help" -> Help(args);
            default -> throw new Exception("Unknown command");
        }
    }



    String helpMessage = """
            Available commands:
            /select <x> <y> - select a square
            /rotate - rotate the selected ship
            /place - place the selected ship
            /ready - ready up
            /help - show this message""";
    private void Help(String[] args)
    {
        addNewMessage(helpMessage, "[System]: ");
    }

    private void Ready(String[] args) throws Exception {
        readyClickHandle(null);
        addNewMessage("Ready Command", "[System]: ");
    }

    private void Place(String[] args) throws Exception {
        addNewMessage("Place Command", "[System]: ");
    }

    private void Rotate(String[] args) throws Exception {
        addNewMessage("Rotate Command", "[System]: ");
    }

    private void Select(String[] args) throws Exception {
        addNewMessage("Select Command", "[System]: ");
    }

    private void createBoard(GridPane grid) {
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                var square = new BoardSquare();
                grid.add(square, i, j);
            }
        }
    }
    @FXML
    public void readyClickHandle(ActionEvent event) throws IOException {
        System.out.println(GlobalGameState.initialShips.get(0).getBoardCoordinates());

        Stage stage = (Stage) readyButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/GameView.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    private boolean areShipsInValidPosition() {
        HashSet<Coordinate> coordinates = new HashSet<>();

        for (var ship : GlobalGameState.initialShips) {
            for (Coordinate coordinate : ship.getBoardCoordinates()) {
                if (coordinates.contains(coordinate)) {
                    return false;
                }
                coordinates.add(coordinate);
            }
        }
        return true;
    }

    private void randomizeShips() {
        while(true){
            for(var ship : GlobalGameState.initialShips){
                var lenght = ship.getLength();

                var random = (int)(Math.random() * 10);

                var randomX = (random-lenght);
                var randomY = (int)(Math.random() * 10);


                ship.setBoardCoordinates(new Coordinate(randomX, randomY));
            }
            if(areShipsInValidPosition()){
                break;
            }
        }
    }


    private void initializeGrid(GridPane grid) {
        grid.setHgap(4);
        grid.setVgap(4);

        randomizeShips();

        for(var ship : GlobalGameState.initialShips){
            ship.addToGrid(grid);
        }
    }
}
