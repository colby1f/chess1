package service;

import dataaccess.AlreadyTakenException;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.*;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

    @Test
    void registerSuccess() throws AlreadyTakenException, DataAccessException {
        var registerRequest = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");
        var user = new UserData("Darrow", "Reaper", "Darrow@mars.com");
        var dataAccess = new MemoryDataAccess();
        var service = new UserService(dataAccess);
        RegisterResult registerResult = service.register(registerRequest);

        assertEquals(dataAccess.getUser("Darrow"), user);
    }

    @Test
    void registerBadRequest() {
        RegisterRequest registerRequest1 = new RegisterRequest("Darrow", "Reaper", null);
        RegisterRequest registerRequest2 = new RegisterRequest("Virginia", null, "Virginia@mars.com");
        RegisterRequest registerRequest3 = new RegisterRequest(null, "Goblin", "Sevro@mars.com");

        var dataAccess = new MemoryDataAccess();
        var service = new UserService(dataAccess);

        assertThrows(DataAccessException.class, () -> {
            service.register(registerRequest1);
        });

        assertThrows(DataAccessException.class, () -> {
            service.register(registerRequest2);
        });

        assertThrows(DataAccessException.class, () -> {
            service.register(registerRequest3);
        });
    }

    @Test
    void registerAlreadyTaken() {
        RegisterRequest registerRequest1 = new RegisterRequest("Darrow", "Reaper", "Darrow@mars.com");
        RegisterRequest registerRequest2 = new RegisterRequest("Darrow", "Brother", "Reaper@mars.com");

        var dataAccess = new MemoryDataAccess();
        var service = new UserService(dataAccess);

        assertThrows(AlreadyTakenException.class, () -> {
            service.register(registerRequest1);
            service.register(registerRequest2);
        });
    }

}
