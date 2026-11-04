package dataaccess.sql;

import chess.ChessGame;
import dataaccess.AlreadyTakenException;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.*;

import static org.junit.jupiter.api.Assertions.*;

public class SQLTests {

    private static DataAccess dataAccess;

    @BeforeAll
    static void openDAO() throws DataAccessException {
        dataAccess = new MySqlDataAccess();
    }

    @AfterEach
    void clear() throws DataAccessException {
        dataAccess.clear();
    }

    @Test
    void addUser() throws DataAccessException {

        var userData = new UserData("Darrow", "Reaper", "Darrow@mars.com");

        dataAccess.addUser(userData);

        var userDataNew = dataAccess.getUser("Darrow");

        assertEquals("Darrow", userDataNew.username());

    }


    @Test
    void addAuth() throws DataAccessException {

        var authData = new AuthData("Mars", "Darrow");

        dataAccess.addAuth(authData);

        var authDataNew = dataAccess.getAuth("Mars");

        assertEquals("Darrow", authDataNew.username());

    }


    @Test
    void removeAuth() throws DataAccessException {

        var authData = new AuthData("Mars", "Darrow");

        dataAccess.addAuth(authData);

        var authDataNew = dataAccess.getAuth("Mars");

        dataAccess.removeAuth(authDataNew);

        assertNull(dataAccess.getAuth("Mars"));

    }


    @Test
    void authenticate() throws DataAccessException {

        var authData = new AuthData("Mars", "Darrow");

        dataAccess.addAuth(authData);

        assertDoesNotThrow(() -> dataAccess.authenticate("Mars"));

    }


    @Test
    void addGame() throws DataAccessException {

        dataAccess.addGame("Institute");

        var games = dataAccess.getGames();

        var game = games.getFirst();

        assertEquals("Institute", game.gameName());

    }


    @Test
    void addPlayer() throws DataAccessException, AlreadyTakenException {

        var authData = new AuthData("Mars", "Darrow");
        dataAccess.addAuth(authData);

        var gameData = dataAccess.addGame("Institute");

        dataAccess.addPlayer(authData.authToken(), ChessGame.TeamColor.WHITE, gameData.gameID());

        var games = dataAccess.getGames();

        var game = games.getFirst();

        assertEquals("Darrow", game.whiteUsername());

    }


    @Test
    void addPlayerAlreadyTaken() throws DataAccessException, AlreadyTakenException {

        var authData1 = new AuthData("Mars", "Darrow");
        dataAccess.addAuth(authData1);

        var authData2 = new AuthData("Luna", "Mustang");
        dataAccess.addAuth(authData2);

        var gameData = dataAccess.addGame("Institute");

        dataAccess.addPlayer(authData1.authToken(), ChessGame.TeamColor.WHITE, gameData.gameID());

        assertThrows(AlreadyTakenException.class, () -> dataAccess.addPlayer(authData2.authToken(), ChessGame.TeamColor.WHITE, gameData.gameID()));

    }


    @Test
    void login() throws DataAccessException {

        var userData = new UserData("Darrow", "Reaper", "Darrow@mars.com");

        dataAccess.addUser(userData);

        assertDoesNotThrow(() -> dataAccess.login("Darrow", "Reaper"));
    }

    @Test
    void loginUnauthorized() throws DataAccessException {

        var userData = new UserData("Darrow", "Reaper", "Darrow@mars.com");

        dataAccess.addUser(userData);

        assertThrows(UnauthorizedException.class, () -> dataAccess.login("Mustang", "Reaper"));
    }

}
