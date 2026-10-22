package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;

import java.util.Map;


public class Server {

    private final Javalin javalin;
    private final GeneralService generalService;
    private final UserService userService;
    private final GameService gameService;

    public Server() {

        DataAccess dataAccess = new MemoryDataAccess();
        generalService = new GeneralService(dataAccess);
        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);

        javalin = Javalin.create(
                config -> config.staticFiles.add("web"));

        javalin.post("/user", this::registerHandler);
        javalin.delete("/db", this::clearHandler);
        javalin.post("/session", this::loginHandler);
        javalin.delete("/session", this::logoutHandler);
        javalin.get("/game", this::listGamesHandler);
        javalin.post("/game", this::createGameHandler);
        javalin.put("game", this::joinGameHandler);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }


    private void clearHandler(Context ctx) {
        Gson gsonObject = new Gson();

        try {
            generalService.clear();
            ctx.status(200);
        } catch (Exception e) {
            ctx.status(500).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error")));
        }
    }

    private void registerHandler(Context ctx) {
        Gson gsonObject = new Gson();
        var userData = gsonObject.fromJson(ctx.body(), RegisterRequest.class);
        try {
            RegisterResult registerResult = userService.register(userData);
            ctx.status(200).contentType("application/json").result(gsonObject.toJson(registerResult));
        } catch (DataAccessException e) {
            ctx.status(400).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error: bad request")));
        } catch (AlreadyTakenException e) {
            ctx.status(403).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error: Already Taken")));
        } catch (Exception e) {
            ctx.status(500).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error")));
        }
    }

    private void loginHandler(Context ctx) {
        Gson gsonObject = new Gson();
        var userData = gsonObject.fromJson(ctx.body(), LoginRequest.class);
        try {
            LoginResult loginResult = userService.login(userData);
            ctx.status(200).contentType("application/json").result(gsonObject.toJson(loginResult));
        } catch (DataAccessException e) {
            ctx.status(400).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error: bad request")));
        } catch (UnauthorizedException e) {
            ctx.status(401).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error: unauthorized")));
        } catch (Exception e) {
            ctx.status(500).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error")));
        }
    }

    private void logoutHandler(Context ctx) {
        Gson gsonObject = new Gson();
        var authToken = ctx.header("authorization");
        try {
            userService.logout(authToken);
            ctx.status(200);
        } catch (UnauthorizedException e) {
            ctx.status(401).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error: unauthorized")));
        } catch (Exception e) {
            ctx.status(500).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error")));
        }
    }

    private void listGamesHandler(Context ctx) {
        Gson gsonObject = new Gson();
        var authToken = ctx.header("authorization");
        try {
            ListGamesResult listGamesResult = gameService.listGames(authToken);
            ctx.status(200).contentType("application/json").result(gsonObject.toJson(listGamesResult));
        } catch (UnauthorizedException e) {
            ctx.status(401).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error: unauthorized")));
        } catch (Exception e) {
            ctx.status(500).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error")));
        }
    }

    private void createGameHandler(Context ctx) {
        Gson gsonObject = new Gson();
        var authToken = ctx.header("authorization");
        var createGameRequest = gsonObject.fromJson(ctx.body(), CreateGameRequest.class);
        try {
            CreateGameResult createGameResult = gameService.createGame(createGameRequest, authToken);
            ctx.status(200).contentType("application/json").result(gsonObject.toJson(createGameResult));
        } catch (DataAccessException e) {
            ctx.status(400).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error: bad request")));
        } catch (UnauthorizedException e) {
            ctx.status(401).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error: unauthorized")));
        } catch (Exception e) {
            ctx.status(500).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error")));
        }
    }

    private void joinGameHandler(Context ctx) {
        Gson gsonObject = new Gson();
        var authToken = ctx.header("authorization");
        var joinGameRequest = gsonObject.fromJson(ctx.body(), JoinGameRequest.class);
        try {
            gameService.joinGame(joinGameRequest, authToken);
            ctx.status(200);
        } catch (DataAccessException e) {
            ctx.status(400).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error: bad request")));
        } catch (UnauthorizedException e) {
            ctx.status(401).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error: unauthorized")));
        } catch (AlreadyTakenException e) {
            ctx.status(403).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error: already taken")));
        } catch (Exception e) {
            ctx.status(500).contentType("application/json").result(gsonObject.toJson(Map.of("message", "Error")));
        }
    }
}
