package websocket.messages;

import com.google.gson.Gson;

public class Notification extends ServerMessage {

    private final static Gson gson = new Gson();
    private final String notification;

    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.notification = message;
    }

    public String getNotification() {
        return notification;
    }
}
