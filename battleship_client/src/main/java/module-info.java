module com.example.battleship_client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.example.battleship_client to javafx.fxml;
    exports com.example.battleship_client;
    exports com.example.battleship_client.controller;
    opens com.example.battleship_client.controller to javafx.fxml;
}