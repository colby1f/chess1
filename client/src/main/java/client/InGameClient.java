package client;

import server.ResponseException;
import server.ServerFacade;

public class InGameClient implements Client {

    private final Repl repl;
    private final ServerFacade server;

    public InGameClient(Repl repl, String serverUrl) {
        this.repl = repl;
        server = new ServerFacade(serverUrl);
    }


    @Override
    public String eval(String input) {
        return "";
    }


    public String highlight(String... params) {
        if (params.length == 1) {
            // SERVER CALL HERE
            return String.format("Available moves for position %s", params[0]);
        }
        throw new ResponseException("Expected: <position>");
    }


    public String move(String... params) {
        if (params.length == 2) {
            // SERVER CALL HERE
            return String.format("Moved %s%s", params[0], params[1]);
        }
        throw new ResponseException("Expected: <current position> <new position> <optional promotion>");
    }


    public String redraw() {
        // SERVER CALL HERE
        return "Current Board";
    }


    public String changeBoardColors(String... params) {
        if (params.length == 1) {
            // SERVER CALL HERE
            return String.format("Board color changed to %s", params[0]);
        }
        throw new ResponseException("Expected: <color number>");
    }


    public String resign() {
        // SERVER CALL HERE
        return "Resigned";
    }


    public String leaveGame() {
        // SERVER CALL HERE
        return "Left game";
    }


    public String help() {
        return """
                - Highlight Available Moves: "highlight" <position> (ex. a1)
                - Make a Move: "move" <current position> <new position> <optional promotion> (ex b3 a3 q)
                - Redraw Chess Board: "redraw"
                - Change Board Colors: "colors" <color number>
                - Resign: "resign"
                - Leave Game: "leave"
                """;
    }
}
