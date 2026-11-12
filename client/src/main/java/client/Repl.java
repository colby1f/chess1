package client;

import java.util.Objects;
import java.util.Scanner;

import ui.EscapeSequences;

public class Repl {

    private final String url;
    private Client client;
    private String state;
    private String user;
    private String authToken;

    public Repl(String serverUrl) {
        url = serverUrl;
        client = new LoggedOutClient(this, url);
        state = "Logged Out";
        user = null;
        authToken = null;
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }


    public void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + state + ">>>" + EscapeSequences.SET_TEXT_COLOR_BLUE);
    }


    public void setState(String newClient) {
        state = newClient;
        if (Objects.equals(newClient, "Logged Out")) {
            client = new LoggedOutClient(this, url);
        } else if (Objects.equals(newClient, "Logged In")) {
            client = new LoggedInClient(this, url, user);
        } else if (Objects.equals(newClient, "Game")) {
            client = new InGameClient(this, url, user);
        }
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
