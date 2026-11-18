package client;

import java.util.Arrays;

public class InGameClient implements Client {

    private final UserState userState;
    private final String user;
    private final ServerFacade server;

    public InGameClient(UserState userState, String user, String serverUrl) {
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
                case "highlight" -> highlight(params);
                case "move" -> move(params);
                case "redraw" -> redraw();
                case "resign" -> resign();
                case "leave" -> leaveGame();
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public String highlight(String... params) throws ResponseException {
        if (params.length == 1) {
            // SERVER CALL HERE
            return String.format("Available moves for position %s", params[0]);
        }
        throw new ResponseException(400, "Expected: <position>");
    }


    public String move(String... params) throws ResponseException {
        if (params.length == 2) {
            // SERVER CALL HERE
            return String.format("%s did move %s%s", user, params[0], params[1]);
        }
        throw new ResponseException(400, "Expected: <current position> <new position> <optional promotion>");
    }


    public String redraw() {
        ChessBoardPrinter chessBoardPrinter = new ChessBoardPrinter(userState.getGame().game(), userState.getColor());
        chessBoardPrinter.printChessBoard();
        return "Current Board";
    }

    public String resign() {
        // SERVER CALL HERE
        return String.format("%s resigned", user);
    }


    public String leaveGame() throws ResponseException {
        server.leaveGame(userState.getAuthToken());
        userState.setState("Logged In");
        userState.setColor(null);
        userState.setGame(null);
        return String.format("%s left the game", user);
    }


    public String help() {
        return """
                - Highlight Available Moves: "highlight" <position> (ex. a1)
                - Make a Move: "move" <current position> <new position> <optional promotion> (ex b3 a3 q)
                - Redraw Chess Board: "redraw"
                - Resign: "resign"
                - Leave Game: "leave"
                """;
    }
}
