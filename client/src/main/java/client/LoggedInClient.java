package client;

import chess.ChessGame;
import model.*;
import server.ResponseException;
import server.ServerFacade;
import service.CreateGameRequest;
import service.CreateGameResult;
import service.JoinGameRequest;
import service.ListGamesResult;

import java.util.Arrays;

public class LoggedInClient implements Client {

    private final Repl repl;
    private final String user;
    private final ServerFacade server;

    public LoggedInClient(Repl repl, String user, String serverUrl) {
        this.repl = repl;
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
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public String listGames() throws ResponseException {
        ListGamesResult listGamesResult = server.listGames(repl.getAuthToken());
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
            CreateGameResult createGameResult = server.createGame(new CreateGameRequest(params[0]), repl.getAuthToken());
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

            server.joinGame(new JoinGameRequest(color, gameID), repl.getAuthToken());
            return String.format("%s joined game %s as %s", user, params[0], params[1]);
        }
        throw new ResponseException(400, "Expected: <game ID> <color>");
    }


    public String watchGame(String... params) throws ResponseException {
        if (params.length == 1) {
            // SERVER CALL HERE
            return String.format("%s is now watching game %s", user, params[0]);
        }
        throw new ResponseException(400, "Expected: <game ID>");
    }


    public String logout() throws ResponseException {
        server.logout(repl.getAuthToken());
        repl.setState("logout");
        repl.setUser(null);
        repl.setAuthToken(null);
        return String.format("%s logged out", user);
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
