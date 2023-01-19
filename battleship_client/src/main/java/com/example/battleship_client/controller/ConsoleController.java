package com.example.battleship_client.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.util.Arrays;

public class ConsoleController {

    private TextField tfMessage;
    private Button buttonMessage;
    private ScrollPane spMain;
    private VBox vboxMessages;

    public ConsoleController(TextField tfMessage, Button buttonMessage, ScrollPane spMain, VBox vboxMessages) {
        this.tfMessage = tfMessage;
        this.buttonMessage = buttonMessage;
        this.spMain = spMain;
        this.vboxMessages = vboxMessages;
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

    private String helpMessage = """
            Available commands:
            /select <x> <y> - select a square
            /rotate - rotate the selected ship
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
