package client;

import chess.ChessGame;
import server.ResponseException;
import server.ServerFacade;

public class LoggedInClient implements Client {

    private final Repl repl;
    private final ServerFacade server;

    public LoggedInClient(Repl repl, String serverUrl) {
        this.repl = repl;
        server = new ServerFacade(serverUrl);
    }


    @Override
    public String eval(String input) {
        return "";
    }


    public String listGames() {
        // SERVER CALL HERE
        return "";
    }


    public String createGame(String... params) {
        if (params.length == 1) {
            // SERVER CALL HERE
            return String.format("Created game %s. Game ID: %d", params[0], 111); //EDIT THIS 111
        }
        throw new ResponseException("Expected: <game name>");
    }


    public String joinGame(String... params) {
        if (params.length == 2) {
            // SERVER CALL HERE
            return String.format("Joined game %s as %s", params[0], params[1]);
        }
        throw new ResponseException("Expected: <game ID> <color>");
    }


    public String watchGame(String... params) {
        if (params.length == 1) {
            // SERVER CALL HERE
            return String.format("Watching game %s", params[0]);
        }
        throw new ResponseException("Expected: <game ID>");
    }


    public String logout() {
        // SERVER CALL HERE
        return "Logged out";
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
