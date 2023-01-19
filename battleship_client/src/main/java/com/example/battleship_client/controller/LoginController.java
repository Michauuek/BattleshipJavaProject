package com.example.battleship_client.controller;

import com.example.battleship_client.model.Message;
import com.example.battleship_client.networking.DataWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    public Button loginButton;
    @FXML
    public TextField login;
    @FXML
    public Label messageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String message = login.getText();
                if (!message.isBlank()) {
                    changeScreen();
                } else {
                    messageLabel.setText("Please enter username");
                }
            }
        });
    }

    @FXML
    public void loginClickHandle(ActionEvent event) throws IOException {
        if (login.getText().isBlank()) {
            messageLabel.setText("Please enter username");
        } else {
            changeScreen();
        }
    }

    private void changeScreen(){
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/GameSetupView.fxml")));
            stage.setScene(new Scene(root));
            stage.show();
            // set global state
            GlobalGameState.name = this.getName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getName() {
        return login.getText();
    }
}
