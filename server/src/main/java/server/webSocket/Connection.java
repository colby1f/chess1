package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public record Connection(Session session, String gameID) {

    public void send(String message) throws IOException {
        synchronized (session) {
            session.getRemote().sendString(message);
        }
    }

}
