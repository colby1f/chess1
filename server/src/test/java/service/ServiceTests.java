package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Test;

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

        assertThrows(UnauthorizedException.class, () -> gameService.listGames(registerResult.authToken()));
        assertThrows(UnauthorizedException.class, () -> userService.logout(registerResult.authToken()));
    }

    @Test
    void loginSuccess() throws AlreadyTakenException, DataAccessException {
        var registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");

        var dataAccess = new MemoryDataAccess();
        var service = new UserService(dataAccess);
        RegisterResult registerResult = service.register(registerRequest);

        service.logout(registerResult.authToken());

        var loginRequest = new LoginRequest("Darrow", "Reaper");
        LoginResult loginResult = service.login(loginRequest);

        assertEquals("Darrow", loginResult.username());

    }

    @Test
    void loginBadRequest() throws AlreadyTakenException, DataAccessException {
        var registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");

        var dataAccess = new MemoryDataAccess();
        var service = new UserService(dataAccess);
        RegisterResult registerResult = service.register(registerRequest);

        service.logout(registerResult.authToken());

        var loginRequest1 = new LoginRequest(null, "Reaper");
        var loginRequest2 = new LoginRequest("Darrow", null);

        assertThrows(DataAccessException.class, () -> service.login(loginRequest1));
        assertThrows(DataAccessException.class, () -> service.login(loginRequest2));
    }


    @Test
    void loginUnauthorized() throws AlreadyTakenException, DataAccessException {
        var registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");

        var dataAccess = new MemoryDataAccess();
        var service = new UserService(dataAccess);
        RegisterResult registerResult = service.register(registerRequest);

        service.logout(registerResult.authToken());

        var loginRequest1 = new LoginRequest("D", "Reaper");
        var loginRequest2 = new LoginRequest("Darrow", "R");

        assertThrows(UnauthorizedException.class, () -> service.login(loginRequest1));
        assertThrows(UnauthorizedException.class, () -> service.login(loginRequest2));
    }

    @Test
    void logoutTwice() throws AlreadyTakenException, DataAccessException {
        var registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");

        var dataAccess = new MemoryDataAccess();
        var service = new UserService(dataAccess);
        RegisterResult registerResult = service.register(registerRequest);

        service.logout(registerResult.authToken());

        assertThrows(UnauthorizedException.class, () -> service.logout(registerResult.authToken()));
    }

    @Test
    void listGamesSuccess() throws AlreadyTakenException, DataAccessException {

        var registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");

        var dataAccess = new MemoryDataAccess();
        var userService = new UserService(dataAccess);

        RegisterResult registerResult = userService.register(registerRequest);

        var gameService = new GameService(dataAccess);

        CreateGameRequest createGameRequest1 = new CreateGameRequest("first");
        CreateGameRequest createGameRequest2 = new CreateGameRequest("second");

        CreateGameResult createGameResult1 = gameService.createGame(createGameRequest1, registerResult.authToken());
        CreateGameResult createGameResult2 = gameService.createGame(createGameRequest2, registerResult.authToken());

        ListGamesResult listGamesResult = gameService.listGames(registerResult.authToken());

        java.util.ArrayList<GameData> games = listGamesResult.games();

        assertEquals(createGameResult1.gameID(), games.getFirst().gameID());
        assertEquals(createGameResult2.gameID(), games.get(1).gameID());
    }

    @Test
    void listGamesUnauthorized() throws AlreadyTakenException, DataAccessException {
        var registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");

        var dataAccess = new MemoryDataAccess();
        var userService = new UserService(dataAccess);

        RegisterResult registerResult = userService.register(registerRequest);

        var gameService = new GameService(dataAccess);

        CreateGameRequest createGameRequest1 = new CreateGameRequest("first");
        CreateGameRequest createGameRequest2 = new CreateGameRequest("second");

        gameService.createGame(createGameRequest1, registerResult.authToken());
        gameService.createGame(createGameRequest2, registerResult.authToken());

        assertThrows(UnauthorizedException.class, () -> gameService.listGames("hello"));

    }


    @Test
    void createGameBadRequest() throws AlreadyTakenException, DataAccessException {
        var registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");

        var dataAccess = new MemoryDataAccess();
        var userService = new UserService(dataAccess);

        RegisterResult registerResult = userService.register(registerRequest);

        var gameService = new GameService(dataAccess);

        CreateGameRequest createGameRequest1 = new CreateGameRequest(null);

        assertThrows(DataAccessException.class, () -> gameService.createGame(createGameRequest1, registerResult.authToken()));
    }

    @Test
    void createGameUnauthorized() {

        var dataAccess = new MemoryDataAccess();

        var gameService = new GameService(dataAccess);

        CreateGameRequest createGameRequest1 = new CreateGameRequest("game");

        assertThrows(UnauthorizedException.class, () -> gameService.createGame(createGameRequest1, "secret"));
    }


    @Test
    void joinGameSuccess() throws AlreadyTakenException, DataAccessException {
        var registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");

        var dataAccess = new MemoryDataAccess();
        var userService = new UserService(dataAccess);

        RegisterResult registerResult = userService.register(registerRequest);

        var gameService = new GameService(dataAccess);

        CreateGameRequest createGameRequest = new CreateGameRequest("first");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest, registerResult.authToken());

        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, createGameResult.gameID());
        gameService.joinGame(joinGameRequest, registerResult.authToken());

        java.util.ArrayList<GameData> games = gameService.listGames(registerResult.authToken()).games();
        GameData game = games.getFirst();

        assertEquals("Darrow", game.whiteUsername());

    }

    @Test
    void joinGameBadRequest() throws DataAccessException, AlreadyTakenException {
        var registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");

        var dataAccess = new MemoryDataAccess();
        var userService = new UserService(dataAccess);

        RegisterResult registerResult = userService.register(registerRequest);

        var gameService = new GameService(dataAccess);

        CreateGameRequest createGameRequest = new CreateGameRequest("first");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest, registerResult.authToken());

        JoinGameRequest joinGameRequest1 = new JoinGameRequest(ChessGame.TeamColor.WHITE, null);
        JoinGameRequest joinGameRequest2 = new JoinGameRequest(null, createGameResult.gameID());

        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest1, registerResult.authToken()));
        assertThrows(DataAccessException.class, () -> gameService.joinGame(joinGameRequest2, registerResult.authToken()));
    }

    @Test
    void joinGameUnauthorized() throws DataAccessException, AlreadyTakenException {
        var registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");

        var dataAccess = new MemoryDataAccess();
        var userService = new UserService(dataAccess);

        RegisterResult registerResult = userService.register(registerRequest);

        var gameService = new GameService(dataAccess);

        CreateGameRequest createGameRequest = new CreateGameRequest("first");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest, registerResult.authToken());

        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, createGameResult.gameID());

        assertThrows(UnauthorizedException.class, () -> gameService.joinGame(joinGameRequest, "hello"));
    }


}
