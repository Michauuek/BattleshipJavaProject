package com.example.battleship_client.controller;

import com.example.battleship_client.model.Coordinate;
import com.example.battleship_client.model.Ship;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Objects;

public class ConsoleController {

    private TextField tfMessage;
    private Button buttonMessage;
    private ScrollPane spMain;
    private VBox vboxMessages;
    private GridPane board;

    public ConsoleController(TextField tfMessage, Button buttonMessage, ScrollPane spMain, VBox vboxMessages, GridPane board) {
        this.tfMessage = tfMessage;
        this.buttonMessage = buttonMessage;
        this.spMain = spMain;
        this.vboxMessages = vboxMessages;
        this.board = board;

        //auto scroll to bottom
        this.vboxMessages.heightProperty().addListener(observable -> spMain.setVvalue(1D));
    }

    public void addNewMessage(String message) {
        var hbox = createHBox();
        var text = createText(message);

        TextFlow textFlow = new TextFlow(text);
        hbox.getChildren().add(textFlow);
        vboxMessages.getChildren().add(hbox);
        tfMessage.clear();
    }

    public void addNewMessage(String message, String owner) {
        var hbox = createHBox();
        var text = createText(owner+message);

        TextFlow textFlow = new TextFlow(text);
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
    void Execute(String command, String[] args) throws Exception {
        switch(command) {
            case "select" -> Select(args);
            case "rotate" -> Rotate(args);
            case "place" -> Place(args);
            case "ready" -> Ready(args);
            case "help" -> Help(args);
            default -> throw new Exception("Unknown command");
        }
    }
    private void Help(String[] args) {
        addNewMessage(helpMessage, "[System]: ");
    }

    private void Ready(String[] args) throws Exception {
        addNewMessage("Ready Command", "[System]: ");

        Stage stage = (Stage) tfMessage.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/GameView.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void Place(String[] args) throws Exception {
        addNewMessage("Place Command", "[System]: ");

        for(var ship : GlobalGameState.initialShips){
            if(ship.isSelected()){
                //TODO: delete previous ship position and place in new one
            }
        }
    }

    private void Rotate(String[] args) throws Exception {
        addNewMessage("Rotate Command", "[System]: ");

        //TODO: add support for for letters - currently working for numbers only eg. /rotate 0 0

        var position = Arrays.asList(args);
        var coordinate = new Coordinate(Integer.parseInt(position.get(0).toLowerCase()), Integer.parseInt(position.get(1)));

        for(var ship : GlobalGameState.initialShips){
            for(var cr : ship.getBoardCoordinates()) {
                if(cr.getColumn() == (coordinate.getColumn()) && cr.getRow() == (coordinate.getRow())) {
                    ship.rotate(board);
                }
            }
        }
    }

    private Ship Select(String[] args) throws Exception {
        addNewMessage("Select Command", "[System]: ");

        var position = Arrays.asList(args);
        var coordinate = new Coordinate(Integer.parseInt(position.get(0).toLowerCase()), Integer.parseInt(position.get(1)));

        for(var ship : GlobalGameState.initialShips){
            for(var cr : ship.getBoardCoordinates()) {
                if(cr.getColumn() == (coordinate.getColumn()) && cr.getRow() == (coordinate.getRow())) {
                    ship.addBorder();
                    return ship;
                }
            }
        }
        return null;
    }

    private String helpMessage = """
            Available commands:
            /select <x> <y> - select a square
            /rotate <x> <y> - rotate the selected ship
            /place - place the selected ship
            /ready - ready up
            /help - show this message""";

    private HBox createHBox() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);

        hbox.setPadding(new Insets(5, 5, 5, 10));
        return hbox;
    }
    private Text createText(String message) {
        Text text = new Text(message);
        text.setFill(Color.WHITE);
        text.setFont(new Font("Monospaced Regular", 16));
        return text;
    }
}
