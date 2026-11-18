package client;

import java.util.Arrays;

import resultsandrequests.LoginRequest;
import resultsandrequests.LoginResult;
import resultsandrequests.RegisterRequest;
import resultsandrequests.RegisterResult;


public class LoggedOutClient implements Client {

    private final UserState userState;
    private final ServerFacade server;

    public LoggedOutClient(UserState userState, String serverUrl) {
        this.userState = userState;
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
                case "admin_clear" -> clear();
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
            userState.setUser(loginResult.username());
            userState.setAuthToken(loginResult.authToken());
            userState.setState("Logged In");
            return String.format("Logged in as %s", loginResult.username());
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }


    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RegisterResult registerResult = server.register(new RegisterRequest(params[0], params[1], params[2]));
            userState.setUser(registerResult.username());
            userState.setAuthToken(registerResult.authToken());
            userState.setState("Logged In");
            return String.format("New user registered: %s", registerResult.username());
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }


    public String clear() throws ResponseException {
        server.clear();
        return "Data Cleared";
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
