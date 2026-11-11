package client;

public class InGameClient implements Client {

    private final String prompt;

    public InGameClient() {
        this.prompt = "Game>>>";
    }

    @Override
    public String getPrompt() {
        return prompt;
    }
}
