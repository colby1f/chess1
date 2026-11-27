package server.webSocket;

import jakarta.websocket.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    private final Map<Session, String> connections = new ConcurrentHashMap<>();

    public void addConnection(Session session, String username) {
        connections.put(session, username);
    }


    public String remove(Session session) {
        return connections.remove(session);
    }

}
