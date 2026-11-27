package server.webSocket;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections;

    public WebSocketHandler(ConnectionManager connections) {
        this.connections = connections;
    }


    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Game Joined");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        Connection connection = new Connection(ctx.session, command.getAuthToken(), command.getGameID());
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), connection);
            case LEAVE -> leave(connection);
        }
    }


    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Game Left");
        // NEED HERE
    }


    private void connect(String authToken, Connection connection) {
        connections.addConnection(connection, authToken);
        var message = String.format("%s ___fix this___ joined in the game", authToken);
        var notification = new Notification(message);
        connections.messageGame(connection, connection.gameID(), notification);
    }


    private void leave(Connection connection) {
        String authToken = connections.remove(connection);
        if (authToken != null) {
            var message = String.format("%s ___fix this___ left the game", authToken);
            var notification = new Notification(message);
            connections.messageGame(connection.gameID(), notification);
        }
    }


}
