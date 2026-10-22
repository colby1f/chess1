package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;

import java.util.ArrayList;
import java.util.UUID;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTakenException, DataAccessException {

        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();

        if (username == null || password == null || email == null) {
            throw new DataAccessException("Error: bad request");
        }

        UserData user = dataAccess.getUser(registerRequest.username());

        if (user != null) {
            throw new AlreadyTakenException("Error: Already Taken");
        }

        String authToken = generateToken();

        UserData userData = new UserData(username, password, email);
        AuthData authData = new AuthData(authToken, username);

        dataAccess.addUser(userData);
        dataAccess.addAuth(authData);

        return new RegisterResult(username, authToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {

        String username = loginRequest.username();
        String password = loginRequest.password();

        if (username == null || password == null) {
            throw new DataAccessException("Error: Bad Request");
        }

        UserData userData = dataAccess.login(username, password);

        String authToken = generateToken();
        AuthData authData = new AuthData(authToken, username);

        dataAccess.addAuth(authData);

        return new LoginResult(username, authToken);

    }

    public void logout(String authToken) {

        dataAccess.authenticate(authToken);

        AuthData authData = dataAccess.getAuth(authToken);

        dataAccess.removeAuth(authData);
    }


    public ListGamesResult listGames(String authToken) {

        dataAccess.authenticate(authToken);

        ArrayList<GameData> games = dataAccess.getGames();

        return new ListGamesResult(games);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException {

        dataAccess.authenticate(authToken);

        String gameName = createGameRequest.gameName();

        if (gameName == null) {
            throw new DataAccessException("Error: Bad Request");
        }

        GameData gameData = dataAccess.addGame(authToken, gameName);

        return new CreateGameResult(gameData.gameID());
    }


    public void joinGame(JoinGameRequest joinGameRequest, String authToken) throws DataAccessException, AlreadyTakenException {

        dataAccess.authenticate(authToken);

        ChessGame.TeamColor playerColor = joinGameRequest.playerColor();
        Integer gameID = joinGameRequest.gameID();

        if (playerColor == null || gameID == null) {
            throw new DataAccessException("Error: bad request");
        }

        GameData gameData = dataAccess.addPlayer(authToken, playerColor, gameID);
    }

}
