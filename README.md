# Go Network Game

## Project Description

This project is a two-player networked Go game implemented in Java.

The system uses a client-server architecture:

- The server runs as a console-based Java application.
- The clients run as Java Swing GUI applications.
- Two clients connect to the same server using the server IP address and port.
- The server controls the game state, validates moves, manages turns, handles captures, and sends board updates to both clients.

The project was developed for a Computer Networks course.

---

## Technologies Used

- Java
- Java Swing for GUI
- Java Socket Programming
- Maven
- Git / GitHub
- AWS EC2 for server deployment

---

## Main Features

- Two-player online gameplay
- Server accepts two clients
- Player assignment: BLACK and WHITE
- 9x9 Go board
- Turn-based move system
- Invalid move detection
- Capture logic
- Captured-stone counters
- Pass button
- Resign button
- Game over screen
- Play again without closing the application
- Start screen with server IP, port, and player name

---

## Project Structure

```text
GoNetworkGame/
│
├── README.md
├── pom.xml
│
└── src/
    └── main/
        └── java/
            └── go/
                │
                ├── client/
                │   ├── ClientMain.java
                │   └── NetworkClient.java
                │
                ├── server/
                │   ├── ServerMain.java
                │   ├── GameServer.java
                │   └── GameSession.java
                │
                ├── model/
                │   ├── Board.java
                │   ├── GameState.java
                │   ├── Move.java
                │   ├── Player.java
                │   └── Stone.java
                │
                ├── logic/
                │   ├── CaptureChecker.java
                │   ├── GoGameLogic.java
                │   └── MoveValidator.java
                │
                └── ui/
                    ├── BoardPanel.java
                    ├── EndScreen.java
                    ├── GameScreen.java
                    └── StartScreen.java
