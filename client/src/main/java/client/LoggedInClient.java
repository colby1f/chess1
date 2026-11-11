package client;

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

    @Override
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
