package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }


    @Override
    public UserData getUser(String username) {
        try (var conn = DatabaseManager.getConnection()) {

            var statement = "SELECT username, password, email FROM user WHERE username=?";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    return readUser(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void addUser(UserData userData) {
        try (var conn = DatabaseManager.getConnection()) {

            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());

                ps.setString(1, userData.username());
                ps.setString(2, hashedPassword);
                ps.setString(3, userData.email());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {

            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    return readAuth(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void addAuth(AuthData authData) {
        try (var conn = DatabaseManager.getConnection()) {

            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authData.authToken());
                ps.setString(2, authData.username());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void removeAuth(AuthData authData) {
        try (var conn = DatabaseManager.getConnection()) {

            var statement = "DELETE FROM auth WHERE authToken=?";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authData.authToken());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void authenticate(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {

            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new UnauthorizedException("Error: Unauthorized");
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> getGames() {
        ArrayList<GameData> games = new ArrayList<>();

        try (var conn = DatabaseManager.getConnection()) {

            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        GameData gameData = readGame(rs);
                        games.add(gameData);
                    }

                    return games;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public GameData getGame(int gameID) {

        try (var conn = DatabaseManager.getConnection()) {

            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                    return null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public GameData addGame(String gameName) {
        try (var conn = DatabaseManager.getConnection()) {

            var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
            ChessGame game = new ChessGame();
            String json = new Gson().toJson(game);

            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.setNull(1, NULL);
                ps.setNull(2, NULL);
                ps.setString(3, gameName);
                ps.setString(4, json);

                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                int gameID = 0;
                if (rs.next()) {
                    gameID = rs.getInt(1);
                }

                return new GameData(gameID, null, null, gameName, game);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void addPlayer(String authToken, ChessGame.TeamColor playerColor, Integer gameID) throws AlreadyTakenException {
        try (var conn = DatabaseManager.getConnection()) {

            var statement1 = "SELECT whiteUsername, blackUsername FROM game WHERE gameID = ?";
            var statement2 = "UPDATE game SET whiteUsername = ? WHERE gameID = ?";
            String usernameToFind = "whiteUsername";
            if (playerColor == ChessGame.TeamColor.BLACK) {
                statement2 = "UPDATE game SET blackUsername = ? WHERE gameID = ?";
                usernameToFind = "blackUsername";
            }

            try (PreparedStatement ps = conn.prepareStatement(statement1)) {
                ps.setInt(1, gameID);

                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    if (rs.getString(usernameToFind) != null) {
                        throw new AlreadyTakenException("Error: Already Taken");
                    }
                }

            }

            try (PreparedStatement ps = conn.prepareStatement(statement2)) {
                ps.setString(1, getAuth(authToken).username());
                ps.setInt(2, gameID);

                ps.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);

        statement = "TRUNCATE auth";
        executeUpdate(statement);

        statement = "TRUNCATE game";
        executeUpdate(statement);
    }

    @Override
    public void login(String username, String password) throws DataAccessException {
        UserData user = getUser(username);


        if (user == null) {
            throw new UnauthorizedException("Error: Unauthorized");
        }

        if (!BCrypt.checkpw(password, user.password())) {
            throw new UnauthorizedException("Error: Unauthorized");
        }
    }


    private UserData readUser(ResultSet rs) throws SQLException {
        if (rs.next()) {
            var username = rs.getString("username");
            var password = rs.getString("password");
            var email = rs.getString("email");
            return new UserData(username, password, email);
        }

        return null;
    }


    private AuthData readAuth(ResultSet rs) throws SQLException {
        if (rs.next()) {
            var authToken = rs.getString("authToken");
            var username = rs.getString("username");
            return new AuthData(authToken, username);
        }

        return null;
    }


    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var json = rs.getString("game");
        ChessGame game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }


    private void executeUpdate(String statement, Object... parameters) throws DataAccessException {
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
                    rs.getInt(1);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user(
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            PRIMARY KEY (username)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS game(
            gameID INT NOT NULL AUTO_INCREMENT,
            whiteUsername VARCHAR(255) DEFAULT NULL,
            blackUsername VARCHAR(255) DEFAULT NULL,
            gameName VARCHAR(255) NOT NULL,
            game TEXT NOT NULL,
            PRIMARY KEY (gameID)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS auth(
            authToken VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL,
            PRIMARY KEY (authToken)
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
