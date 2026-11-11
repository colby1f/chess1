package client;

public class Repl {

    private final Client client;

    public Repl(String serverUrl) {
        client = new LoggedOutClient();
    }
}
