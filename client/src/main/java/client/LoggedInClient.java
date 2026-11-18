package client;

import chess.ChessGame;
import model.*;
import resultsandrequests.CreateGameRequest;
import resultsandrequests.CreateGameResult;
import resultsandrequests.JoinGameRequest;
import resultsandrequests.ListGamesResult;

import java.util.Arrays;

public class LoggedInClient implements Client {

    private final UserState userState;
    private final String user;
    private final ServerFacade server;

    public LoggedInClient(UserState userState, String user, String serverUrl) {
        this.userState = userState;
        this.user = user;
        server = new ServerFacade(serverUrl);
    }


    @Override
    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");

            String command;
            if (tokens.length > 0) {
                command = tokens[0];
            } else {
                command = "help";
            }

            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (command) {
                case "list" -> listGames();
                case "create" -> createGame(params);
                case "join" -> joinGame(params);
                case "watch" -> watchGame(params);
                case "logout" -> logout();
                case "admin_clear" -> clear();
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getCode() + " " + e.getMessage();
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public String listGames() throws ResponseException {
        ListGamesResult listGamesResult = server.listGames(userState.getAuthToken());
        StringBuilder gameList = new StringBuilder();
        for (GameData game : listGamesResult.games()) {
            gameList.append(game.gameID())
                    .append(". Game name: ")
                    .append(game.gameName())
                    .append("   White: ")
                    .append(game.whiteUsername())
                    .append("   Black: ")
                    .append(game.blackUsername())
                    .append("\n");
        }

        return gameList.toString();
    }


    public String createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            CreateGameResult createGameResult = server.createGame(new CreateGameRequest(params[0]), userState.getAuthToken());
            return String.format("Created game %s. Game ID: %d", params[0], createGameResult.gameID());
        }
        throw new ResponseException(400, "Expected: <game name>");
    }


    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {

            ChessGame.TeamColor color;
            if (params[1].equalsIgnoreCase("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if (params[1].equalsIgnoreCase("black")) {
                color = ChessGame.TeamColor.BLACK;
            } else {
                throw new ResponseException(400, "Color must be black or white");
            }

            int gameID;
            try {
                gameID = Integer.parseInt(params[0]);
            } catch (Exception e) {
                throw new ResponseException(400, "Game ID must be a number");
            }

            GameData gameData = server.joinGame(new JoinGameRequest(color, gameID), userState.getAuthToken());
            ChessBoardPrinter chessBoardPrinter = new ChessBoardPrinter(gameData.game(), color);
            chessBoardPrinter.printChessBoard();
            userState.setGame(gameData);
            userState.setColor(color);
            userState.setState("Game");
            return String.format("%s joined game %s as %s", user, gameID, color);
        }
        throw new ResponseException(400, "Expected: <game ID> <color>");
    }


    public String watchGame(String... params) throws ResponseException {
        if (params.length == 1) {

            int gameID;
            try {
                gameID = Integer.parseInt(params[0]);
            } catch (Exception e) {
                throw new ResponseException(400, "Game ID must be a number");
            }

            ListGamesResult listGamesResult = server.watchGame(userState.getAuthToken());
            if (listGamesResult == null) {
                throw new ResponseException(400, "Game does not exist");
            }

            GameData correct_game = null;
            for (GameData game : listGamesResult.games()) {
                if (game.gameID() == gameID) {
                    correct_game = game;
                }
            }

            if (correct_game == null) {
                throw new ResponseException(400, "Game Does not exist");
            }

            ChessBoardPrinter chessBoardPrinter = new ChessBoardPrinter(correct_game.game(), ChessGame.TeamColor.WHITE);
            chessBoardPrinter.printChessBoard();
            return String.format("%s is now watching game %s", user, correct_game.gameName());
        }
        throw new ResponseException(400, "Expected: <game ID>");
    }


    public String logout() throws ResponseException {
        server.logout(userState.getAuthToken());
        userState.setState("Logged Out");
        userState.setUser(null);
        userState.setAuthToken(null);
        return String.format("%s logged out", user);
    }


    public String clear() throws ResponseException {
        server.clear();
        userState.setState("Logged Out");
        userState.setUser(null);
        userState.setAuthToken(null);
        return "Data Cleared";
    }


    public String help() {
        return """
                - List Games: "list"
                - Create Game: "create" <game name>
                - Join Game: "join" <game ID> <color>
                - Watch Game: "watch" <game ID>
                - List of Commands: "help"
                - Logout: "logout"
                """;
    }
}
