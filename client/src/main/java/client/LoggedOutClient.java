package client;

public class LoggedOutClient implements Client {

    private final String prompt;

    public LoggedOutClient() {
        this.prompt = "Logged Out>>>";
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public String eval(String input) {
        return "";
    }
}
