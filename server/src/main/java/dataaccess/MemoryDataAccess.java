package dataaccess;

import model.*;

import java.util.HashMap;
import java.util.Map;


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
    public void addAuth(AuthData authData) {
        auths.put(authData.username(), authData);
    }

    @Override
    public void clear() {
        users.clear();
        auths.clear();
        games.clear();
    }



}
