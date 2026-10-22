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

        // Register your endpoints and exception handlers here.
        javalin.post("/user", this::registerHandler);
        javalin.delete("/db", this::clearHandler);
        javalin.post("/session", this::loginHandler);
//        javalin.delete("/session", this::logoutHandler);
//        javalin.get("/game", this::listGamesHandler);

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
            ctx.status(500).contentType("application/json").result(gsonObject.toJson(Map.of("Message", "Error")));
        }
    }

    private void registerHandler(Context ctx) {
        Gson gsonObject = new Gson();
        var userData = gsonObject.fromJson(ctx.body(), RegisterRequest.class);
        try {
            RegisterResult registerResult = userService.register(userData);
            ctx.status(200).contentType("application/json").result(gsonObject.toJson(registerResult));
        } catch (AlreadyTakenException e) {
            ctx.status(403).contentType("application/json").result(gsonObject.toJson(Map.of("Message", "Error: Already Taken")));
        } catch (DataAccessException e) {
            ctx.status(400).contentType("application/json").result(gsonObject.toJson(Map.of("Message", "Error: bad request")));
        } catch (Exception e) {
            ctx.status(500).contentType("application/json").result(gsonObject.toJson(Map.of("Message", "Error")));
        }
    }

    private void loginHandler(Context ctx) {
        Gson gsonObject = new Gson();
        var userData = gsonObject.fromJson(ctx.body(), LoginRequest.class);
        try {
            LoginResult loginResult = userService.login(userData);
            ctx.status(200).contentType("application/json").result(gsonObject.toJson(loginResult));
        } catch (DataAccessException e) {
            ctx.status(400).contentType("application/json").result(gsonObject.toJson(Map.of("Message", "Bad Request")));
        } catch (UnauthorizedException e) {
            ctx.status(401).contentType("application/json").result(gsonObject.toJson(Map.of("Message", "Error: unauthorized")));
        } catch (Exception e) {
            ctx.status(500).contentType("application/json").result(gsonObject.toJson(Map.of("Message", "Error")));
        }
    }

//    private void logoutHandler(Context ctx) {
//        Gson gsonObject = new Gson();
//        var authToken = gsonObject.fromJson(ctx.header(), String);
//    }

//    private void listGamesHandler(Context ctx) {
//        Gson gsonObject = new Gson();
//        var authToken = gsonObject.fromJson(ctx.header(), String);
//    }
}
