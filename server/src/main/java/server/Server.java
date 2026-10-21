package server;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;


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

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }


    private void clearHandler(Context ctx) {
        generalService.clear();
    }

}
