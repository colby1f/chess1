package client;

public class LoggedInClient implements Client {

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
