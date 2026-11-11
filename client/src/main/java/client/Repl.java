package client;

import java.util.Scanner;

import ui.EscapeSequences;

public class Repl {

    private final Client client;

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
        System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + client.getPrompt());
    }
}
