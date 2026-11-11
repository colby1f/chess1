package client;

import java.util.Arrays;
import server.ResponseException;
import server.ServerFacade;

public class LoggedOutClient implements Client {

    private final Repl repl;
    private final ServerFacade server;

    public LoggedOutClient(Repl repl, String serverUrl) {
        this.repl = repl;
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
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public String login(String... params) {
        if (params.length == 2) {
            repl.setUser(params[0]);
            repl.setState("Logged In");
            // SERVER CALL HERE
            return String.format("Logged in as %s", params[0]);
        }
        throw new ResponseException("Expected: <username> <password>");
    }


    public String register(String... params) {
        if (params.length == 3) {
            repl.setUser(params[0]);
            repl.setState("Logged In");
            // SERVER CALL HERE
            return String.format("New user registered: %s", params[0]);
        }
        throw new ResponseException("Expected: <username> <password> <email>");
    }


    public String help() {
        return """
                - Login: "login" <username> <password>
                - Register: "register" <username> <password> <email>
                - Exit: "quit"
                - List of Commands: "help"
                """;
    }
}
