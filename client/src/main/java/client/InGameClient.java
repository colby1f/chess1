package client;

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


    @Override
    public String help() {
        return """
                - Highlight Available Moves: "highlight" <position> (ex. a1)
                - Make a Move: "move" <current position> <new position> <optional promotion> (ex b3 a3 q)
                - Redraw Chess Board: "redraw"
                - Change Colors: "colors" <color number>
                - Resign: "resign"
                - Leave Game: "leave"
                """;
    }
}
