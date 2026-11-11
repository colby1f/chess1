package client;

import java.util.Objects;
import java.util.Scanner;

import ui.EscapeSequences;

public class Repl {

    private Client client;

    public Repl(String serverUrl) {
        client = new LoggedOutClient();
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess");


        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")) {
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
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + client.getPrompt() + EscapeSequences.SET_TEXT_COLOR_BLUE);
    }


    public void setClient(String newClient) {
        if (Objects.equals(newClient, "Logged Out")) {
            client = new LoggedOutClient();
        }
        else if (Objects.equals(newClient, "Logged In")) {
            client = new LoggedInClient();
        }
        else if (Objects.equals(newClient, "In Game")) {
            client = new InGameClient();
        }
    }
}
