package server.webSocket;

import com.google.gson.Gson;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;
import jdk.jshell.spi.ExecutionControl;
import org.eclipse.jetty.server.Authentication;
import websocket.commands.UserGameCommand;

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





}
