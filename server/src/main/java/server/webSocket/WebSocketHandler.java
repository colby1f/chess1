package server.webSocket;

import com.google.gson.Gson;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;
import websocket.commands.UserGameCommand;
import jakarta.websocket.*;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

public class WebSocketHandler {

    private final ConnectionManager connections;

    public WebSocketHandler(ConnectionManager connections) {
        this.connections = connections;
    }


    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Game Joined");
        ctx.enableAutomaticPings();
    }

    public void handleMessage(WsMessageContext ctx) {
        UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        switch (command.getCommandType()) {
            // NEED HERE
        }
    }

    public void handleClose(WsCloseContext ctx) {
        System.out.println("Game Left");
        // NEED HERE
    }


    private void connect(String username, Connection connection) {
        connections.addConnection(connection, username);
        var message = String.format("%s joined in the game", username);
        var notification = new Notification(message);
        connections.messageGame(connection, connection.gameID(), notification);
    }


}
