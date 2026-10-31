package dataaccess;

import chess.ChessGame;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DataAccess {
    UserData getUser(String username) throws DataAccessException;

    void addUser(UserData userData) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void addAuth(AuthData authData) throws DataAccessException;

    void removeAuth(AuthData authData) throws DataAccessException;

    void authenticate(String authToken) throws DataAccessException;

    ArrayList<GameData> getGames() throws DataAccessException;

    GameData addGame(String gameName) throws DataAccessException;

    void addPlayer(String authToken, ChessGame.TeamColor playerColor, Integer gameID) throws AlreadyTakenException;

    void clear() throws DataAccessException;

    void login(String username, String password);


}
