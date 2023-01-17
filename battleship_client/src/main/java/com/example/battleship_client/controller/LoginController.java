package com.example.battleship_client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    public Button loginButton;
    @FXML
    public TextField login;
    @FXML
    public Label messageLabel;
    @FXML
    public void loginClickHandle(ActionEvent event) throws IOException {
        if (this.login.getText().isBlank()) {
            this.messageLabel.setText("Please enter username");
        } else {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/GameSetupView.fxml")));

            stage.setScene(new Scene(root));
            stage.show();
            // set global state
            GlobalGameState.name = this.getName();
        }
    }
    public String getName() {
        return login.getText();
    }
}
