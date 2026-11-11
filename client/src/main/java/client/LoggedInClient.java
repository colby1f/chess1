package client;

public class LoggedInClient implements Client {
    private final String prompt;

    public LoggedInClient() {
        this.prompt = "Logged In>>>";
    }

    @Override
    public String getPrompt() {
        return prompt;
    }
}
