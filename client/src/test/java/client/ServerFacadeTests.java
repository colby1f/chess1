package client;

import org.junit.jupiter.api.*;
import server.ResponseException;
import server.Server;
import server.ServerFacade;
import service.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void clear() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void loginPositive() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");
        RegisterResult registerResult = facade.register(registerRequest);

        facade.logout(registerResult.authToken());

        LoginRequest loginRequest = new LoginRequest(registerResult.username(), registerRequest.password());
        LoginResult loginResult = facade.login(loginRequest);

        assert (Objects.equals(loginResult.username(), "Darrow"));
    }

    @Test
    public void loginNegative() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");
        RegisterResult registerResult = facade.register(registerRequest);

        facade.logout(registerResult.authToken());

        LoginRequest loginRequest = new LoginRequest(registerResult.username(), "wrongPassword");
        assertThrows(ResponseException.class, () -> facade.login(loginRequest));
    }


    @Test
    public void clearPositive() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");
        RegisterResult registerResult = facade.register(registerRequest);

        facade.clear();

        LoginRequest loginRequest = new LoginRequest(registerResult.username(), registerRequest.password());
        assertThrows(ResponseException.class, () -> facade.login(loginRequest));
    }


    @Test
    public void listGamesPositive() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");
        RegisterResult registerResult = facade.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest("institute");
        CreateGameResult createGameResult = facade.createGame(createGameRequest, registerResult.authToken());

        ListGamesResult listGamesResult = facade.listGames(registerResult.authToken());
        assert (listGamesResult.games().getFirst().gameID() == createGameResult.gameID());
    }
}
