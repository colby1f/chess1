package client;

import java.util.Scanner;

import ui.EscapeSequences;

public class Repl {

    private final UserState userState;

    public Repl(String serverUrl) {
        this.userState = new UserState(serverUrl);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess");
        System.out.print(userState.getClient().help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = userState.getClient().eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.print(result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }


    public void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + userState.getState() + ">>> " + EscapeSequences.SET_TEXT_COLOR_BLUE);
    }
}
