package server.webSocket;

import jakarta.websocket.*;

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

}
