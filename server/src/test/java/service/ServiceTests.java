package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Test;

import javax.naming.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    @Test
    void registerSuccess() throws AlreadyTakenException, DataAccessException {
        var registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");
        var user = new UserData("Darrow", "Reaper", "Darrow@mars.com");
        var dataAccess = new MemoryDataAccess();
        var service = new UserService(dataAccess);
        service.register(registerRequest);

        assertEquals(dataAccess.getUser("Darrow"), user);
    }

    @Test
    void registerBadRequest() {
        RegisterRequest registerRequest1 = new RegisterRequest("Darrow", "Reaper", null);
        RegisterRequest registerRequest2 = new RegisterRequest("Virginia", null, "Virginia@mars.com");
        RegisterRequest registerRequest3 = new RegisterRequest(null, "Goblin", "Sevro@mars.com");

        var dataAccess = new MemoryDataAccess();
        var service = new UserService(dataAccess);

        assertThrows(DataAccessException.class, () -> service.register(registerRequest1));

        assertThrows(DataAccessException.class, () -> service.register(registerRequest2));

        assertThrows(DataAccessException.class, () -> service.register(registerRequest3));
    }

    @Test
    void registerAlreadyTaken() {
        RegisterRequest registerRequest1 = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");
        RegisterRequest registerRequest2 = new RegisterRequest("Darrow", "Brother", "Reaper@mars.com");

        var dataAccess = new MemoryDataAccess();
        var service = new UserService(dataAccess);

        assertThrows(AlreadyTakenException.class, () -> {
            service.register(registerRequest1);
            service.register(registerRequest2);
        });
    }


    @Test
    void clearSuccess() throws AlreadyTakenException, DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");
        CreateGameRequest createGameRequest = new CreateGameRequest("Institute");

        var dataAccess = new MemoryDataAccess();
        var userService = new UserService(dataAccess);
        var gameService = new GameService(dataAccess);
        var generalService = new GeneralService(dataAccess);

        RegisterResult registerResult = userService.register(registerRequest);
        CreateGameResult createGameResult = gameService.createGame(createGameRequest, registerResult.authToken());

        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, createGameResult.gameID());

        gameService.joinGame(joinGameRequest, registerResult.authToken());

        generalService.clear();

        assertThrows(AuthenticationException.class, () -> {
            gameService.listGames(registerResult.authToken());
            userService.logout(registerResult.authToken());
        });
    }



}
