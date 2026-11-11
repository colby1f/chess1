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

        }

    }


    public void printPrompt() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + client.getPrompt());
    }
}
