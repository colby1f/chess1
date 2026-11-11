package client;

import java.util.Objects;
import java.util.Scanner;

import ui.EscapeSequences;

public class Repl {

    private Client client;
    private String state;

    public Repl(String serverUrl) {
        client = new LoggedOutClient(this);
        state = "Logged Out";
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + result);
            } catch (Throwable e) {
                System.out.print(e.toString());
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
            client = new LoggedOutClient(this);
        } else if (Objects.equals(newClient, "Logged In")) {
            client = new LoggedInClient(this);
        } else if (Objects.equals(newClient, "Game")) {
            client = new InGameClient(this);
        }
    }
}
