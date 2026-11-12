package client;

import java.util.Arrays;

import server.ResponseException;
import server.ServerFacade;
import service.*;


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


    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginResult loginResult = server.login(new LoginRequest(params[0], params[1]));
            repl.setUser(loginResult.username());
            repl.setAuthToken(loginResult.authToken());
            repl.setState("Logged In");
            return String.format("Logged in as %s", loginResult.username());
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }


    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RegisterResult registerResult = server.register(new RegisterRequest(params[0], params[1], params[2]));
            repl.setUser(registerResult.username());
            repl.setAuthToken(registerResult.authToken());
            repl.setState("Logged In");
            return String.format("New user registered: %s", registerResult.username());
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
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
