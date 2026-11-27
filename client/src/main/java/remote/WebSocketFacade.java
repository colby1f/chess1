package remote;

import client.ResponseException;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade {

    Session session;

    public WebSocketFacade(String url) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, socketURI);

            session.addMessageHandler(String.class, message -> {
                ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
            });

        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new ResponseException(400, e.getMessage()); // CHECK THIS CODE
        }
    }


}
