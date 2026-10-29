package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import javax.xml.crypto.Data;
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
    public void addUser(UserData userData) {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";

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
    public GameData addGame(String authToken, String gameName) {
        return null;
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


    private int executeUpdate(String statement, Object... parameters) throws DataAccessException, SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < parameters.length; i++) {
                    Object parameter = parameters[i];
                    if (parameter instanceof String p) ps.setString(i + 1, p);
                    else if (parameter instanceof Integer p) ps.setInt(i + 1, p);
                    else if (parameter instanceof ChessGame p) ps.setString(i + 1, p.toString());
                    else if (parameter == null) ps.setNull(i + 1, NULL);
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
