package websocket.messages;

import com.google.gson.Gson;

public class Notification extends ServerMessage {

    private final static Gson gson = new Gson();
    private final String notification;

    public Notification(String notification) {
        super(ServerMessageType.NOTIFICATION);
        this.notification = notification;
    }

    public String getNotification() {
        return notification;
    }
}
