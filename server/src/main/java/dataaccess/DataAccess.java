package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.ArrayList;

public interface DataAccess {
    UserData getUser(String username);

    void addUser(UserData userData);

    AuthData getAuth(String authToken);

    void addAuth(AuthData authData);

    void removeAuth(AuthData authData);

    void authenticate(String authToken);

    ArrayList<GameData> getGames();

    GameData addGame(String authToken, String gameName);

    GameData addPlayer(String authToken, ChessGame.TeamColor playerColor, Integer gameID) throws AlreadyTakenException;

    void clear();

    UserData login(String username, String password);


}
