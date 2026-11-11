package client;

public class LoggedInClient implements Client {

    private final Repl repl;

    public LoggedInClient(Repl repl) {
        this.repl = repl;
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
