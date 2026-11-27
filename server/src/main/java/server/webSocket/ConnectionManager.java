package server.webSocket;

import jakarta.websocket.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    private final Map<Connection, String> connections = new ConcurrentHashMap<>();

    public void addConnection(Connection connection, String username) {
        connections.put(connection, username);
    }


    public String remove(Connection connection) {
        return connections.remove(connection);
    }


    public void messageGame(int gameID, Notification notification) {
        messageGame(null, gameID, notification);
    }

    public void messageGame(Connection excludeConnection, int gameID, Notification notification) {
        String message = notification.getNotification();
        for (Connection connection : connections.keySet()) {
            if (connection.gameID() != gameID) {
                continue;
            }
            if (connection.equals(excludeConnection)) {
                continue;
            }
            if (!connection.isOpen()) {
                connections.remove(connection);
                continue;
            }
            try {
                connection.send(message);
            } catch (IOException e) {
                connections.remove(connection);
            }
        }
    }

}
