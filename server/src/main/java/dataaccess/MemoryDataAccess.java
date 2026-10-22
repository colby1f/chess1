package dataaccess;

import model.*;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MemoryDataAccess implements DataAccess {

    private final Map<String, UserData> users = new HashMap<>();
    private final Map<String, AuthData> auths = new HashMap<>();
    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void addUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }

    @Override
    public void addAuth(AuthData authData) {
        auths.put(authData.username(), authData);
    }

    @Override
    public void removeAuth(AuthData authData) {
        String authToken = authData.authToken();

        auths.remove(authToken);
    }

    @Override
    public void clear() {
        users.clear();
        auths.clear();
        games.clear();
    }

    @Override
    public UserData login(String username, String password) {
        UserData user = users.get(username);

        if (!Objects.equals(user.password(), password)) {
            throw new UnauthorizedException("Error: Unauthorized");
        }

        return user;
    }

    @Override
    public void authenticate(String authToken) {
        AuthData authData = auths.get(authToken);

        if (authData == null) {
            throw new UnauthorizedException("Error: Unauthorized");
        }
    }

    @Override
    public ArrayList<GameData> getGames() {
        return new ArrayList<>(games.values());
    }

}
