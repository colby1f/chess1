package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MemoryDataAccess implements DataAccess {

    private final Map<String, UserData> users = new HashMap<>();
    private final Map<String, AuthData> auths = new HashMap<>();
    private final Map<Integer, GameData> games = new HashMap<>();
    private int availableGameID = 1;

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
    public void login(String username, String password) {
        UserData user = users.get(username);

        if (user == null) {
            throw new UnauthorizedException("Error: Unauthorized");
        }

        if (!Objects.equals(user.password(), password)) {
            throw new UnauthorizedException("Error: Unauthorized");
        }
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

    @Override
    public GameData addGame(String authToken, String gameName) {

        GameData gameData = new GameData(availableGameID, null, null, gameName, new ChessGame());

        games.put(availableGameID, gameData);

        availableGameID++;

        return gameData;
    }

    @Override
    public void addPlayer(String authToken, ChessGame.TeamColor playerColor, Integer gameID) throws AlreadyTakenException {

        GameData gameData = games.get(gameID);

        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();
        String gameName = gameData.gameName();
        ChessGame game = gameData.game();

        if (playerColor == ChessGame.TeamColor.WHITE) {
            if (whiteUsername != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            whiteUsername = getAuth(authToken).username();
        } else {
            if (blackUsername != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            blackUsername = getAuth(authToken).username();
        }

        games.put(gameID, new GameData(gameID, whiteUsername, blackUsername, gameName, game));

    }

}
