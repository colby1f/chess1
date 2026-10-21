package service;

import dataaccess.DataAccess;

public class GameService {

    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public ListGamesResult listGames() {
        return null;
    }

    public CreateGameResult createGame(CreateGameRequest createRequest) {
        return null;
    }

    public void joinGame(JoinGameRequest joinRequest) {
    }
}
