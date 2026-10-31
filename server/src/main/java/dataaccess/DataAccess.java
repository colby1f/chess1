package dataaccess;

import chess.ChessGame;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DataAccess {
    UserData getUser(String username);

    void addUser(UserData userData) throws DataAccessException, SQLException;

    AuthData getAuth(String authToken);

    void addAuth(AuthData authData) throws DataAccessException;

    void removeAuth(AuthData authData);

    void authenticate(String authToken);

    ArrayList<GameData> getGames();

    GameData addGame(String gameName) throws DataAccessException, SQLException;

    void addPlayer(String authToken, ChessGame.TeamColor playerColor, Integer gameID) throws AlreadyTakenException;

    void clear();

    void login(String username, String password);


}
