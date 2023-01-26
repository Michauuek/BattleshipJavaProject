# BattleshipJavaProject

This university project is a game of Battleships where the server is written in Java 19 using networking (TCP), virtual thread, and JDBC with MySQL. 
The client, on the other hand, is written in JavaFX using the Model-View-Controller (MVC) pattern. 
The server handles the game logic and communicates with the client using TCP and JDBC with MySQL used for data storage. 


This game of Battleships can be played in two ways: using a mouse or using keyboard commands on the terminal.
The game features a user-friendly interface that allows players to interact with the game using a mouse to place ships and fire on the opponent's ships. 
Additionally, players can also play the game by sending commands on the terminal. 
This allows for a more traditional, text-based experience of the game, and is perfect for players who prefer to use keyboard commands. 
The game has been designed to be flexible and cater to different playing styles, making it a unique and enjoyable experience for all players.

## Screens
Game setup
![image](https://user-images.githubusercontent.com/95347958/214853138-a3810b0b-f47c-4495-893a-a3550e986ae4.png)

Game with your turn
![image](https://user-images.githubusercontent.com/95347958/214853441-b3620315-ecc7-4777-8e82-641833696638.png)

Game with enemy at the move
![image](https://user-images.githubusercontent.com/95347958/214853589-64c9ff05-d6a5-47e2-82cf-f658e72b4efe.png)

## Available commands:
- /select <x> <y> - select a square
- /rotate <x> <y> - rotate the selected ship
- /place <x> <y> - place the selected ship
- /ready - ready up
- /help - show this message
