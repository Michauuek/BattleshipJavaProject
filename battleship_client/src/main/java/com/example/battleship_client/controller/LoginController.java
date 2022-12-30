package com.example.battleship_client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    public Button loginButton;
    @FXML
    public TextField login;
    @FXML
    public Label messageLabel;
    @FXML
    public void loginClickHandle(ActionEvent event) {
        if (this.login.getText().equals("")) {
            this.messageLabel.setText("Please enter username");
        } else {
            //TODO przejscie do ekranu gry
        }
    }
}
