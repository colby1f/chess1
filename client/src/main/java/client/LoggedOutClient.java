package client;

import java.util.Arrays;
import java.util.Locale;

public class LoggedOutClient implements Client {

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

            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public String login(String... params) {
        return "";
    }



    public String help() {

    }
}
