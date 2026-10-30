package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws DataAccessException, SQLException {
        configureDatabase();
    }


    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void addAuth(AuthData authData) {

    }

    @Override
    public void removeAuth(AuthData authData) {

    }

    @Override
    public void authenticate(String authToken) {

    }

    @Override
    public ArrayList<GameData> getGames() {
        return null;
    }

    @Override
    public GameData addGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        String json = new Gson().toJson(game);
        int gameID = executeUpdate(statement, null, null, gameName, json);
        return new GameData(gameID, null, null, gameName, game);
    }

    @Override
    public void addPlayer(String authToken, ChessGame.TeamColor playerColor, Integer gameID) throws AlreadyTakenException {

    }

    @Override
    public void clear() {

    }

    @Override
    public void login(String username, String password) {

    }


    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }


    private AuthData readAuth(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
    }


    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var json = rs.getString("json");
        ChessGame game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }


    private int executeUpdate(String statement, Object... parameters) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < parameters.length; i++) {
                    Object parameter = parameters[i];
                    if (parameter instanceof String p) {
                        ps.setString(i + 1, p);
                    } else if (parameter instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    } else if (parameter instanceof ChessGame p) {
                        ps.setString(i + 1, p.toString());
                    } else if (parameter == null) {
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user(
            'username' String NOT NULL,
            'password' String NOT NULL,
            'email' String NOT NULL,
            PRIMARY KEY ('username')
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS game(
            'gameID' int NOT NULL AUTO_INCREMENT,
            'whiteUsername' String DEFAULT NULL,
            'blackUsername' String DEFAULT NULL,
            'gameName' String NOT NULL,
            'game' ChessGame NOT NULL,
            PRIMARY KEY ('gameID')
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS auth(
            'authToken' STRING NOT NULL,
            'username' STRING NOT NULL,
            PRIMARY KEY ('authToken')
            )
            """

    };


    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
