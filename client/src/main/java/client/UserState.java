package client;

import chess.ChessGame;
import model.GameData;

import java.util.Objects;

public class UserState {

    private final String url;
    private Client client;
    private String state;
    private String user;
    private String authToken;
    private GameData game;
    private ChessGame.TeamColor color;

    public UserState(String serverUrl) {
        url = serverUrl;
        client = new LoggedOutClient(this, url);
        state = "Logged Out";
        user = null;
        authToken = null;
        game = null;
        color = null;
    }


    public void setState(String newClient) {
        state = newClient;
        if (Objects.equals(newClient, "Logged Out")) {
            client = new LoggedOutClient(this, url);
        } else if (Objects.equals(newClient, "Logged In")) {
            client = new LoggedInClient(this, user, url);
        } else if (Objects.equals(newClient, "Game")) {
            client = new InGameClient(this, user, url);
        }
    }

    public String getState() {
        return state;
    }

    public Client getClient() {
        return client;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setGame(GameData game) {
        this.game = game;
    }

    public GameData getGame() {
        return game;
    }

    public void setColor(ChessGame.TeamColor color) {
        this.color = color;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }
}
