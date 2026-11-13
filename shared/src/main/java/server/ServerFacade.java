package server;

import com.google.gson.Gson;

import model.*;
import service.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        var httpRequest = buildRequest("POST", "/session", loginRequest, null);
        var response = sendRequest(httpRequest);
        return handleResponse(response, LoginResult.class);
    }


    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {
        var httpRequest = buildRequest("POST", "/user", registerRequest, null);
        var response = sendRequest(httpRequest);
        return handleResponse(response, RegisterResult.class);
    }


    public ListGamesResult listGames(String authToken) throws ResponseException {
        var httpRequest = buildRequest("GET", "/game", null, authToken);
        var response = sendRequest(httpRequest);
        return handleResponse(response, ListGamesResult.class);
    }


    public CreateGameResult createGame(CreateGameRequest createGameRequest, String authToken) throws ResponseException {
        var httpRequest = buildRequest("POST", "/game", createGameRequest, authToken);
        var response = sendRequest(httpRequest);
        return handleResponse(response, CreateGameResult.class);
    }


    public void joinGame(JoinGameRequest joinGameRequest, String authToken) throws ResponseException {
        var httpRequest = buildRequest("POST", "/game", joinGameRequest, authToken);
        sendRequest(httpRequest);
    }


    private HttpRequest buildRequest(String method, String path, Object body, String header) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));

        if (header != null) {
            request.setHeader("authentication", header);
        }

        return request.build();
    }


    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }


    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (status / 100 != 2) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(status, "Error: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

}
