package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;

import java.util.ArrayList;

public class GameService {

    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
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

        GameData gameData = dataAccess.addGame(gameName);

        return new CreateGameResult(gameData.gameID());
    }

    public void joinGame(JoinGameRequest joinGameRequest, String authToken) throws DataAccessException, AlreadyTakenException {

        dataAccess.authenticate(authToken);

        ChessGame.TeamColor playerColor = joinGameRequest.playerColor();
        Integer gameID = joinGameRequest.gameID();

        if (playerColor == null || gameID == null) {
            throw new DataAccessException("Error: bad request");
        }

        dataAccess.addPlayer(authToken, playerColor, gameID);
    }
}
