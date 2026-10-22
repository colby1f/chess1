package service;

import dataaccess.*;
import model.*;

import java.util.UUID;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTakenException {

        UserData user = dataAccess.getUser(registerRequest.username());

        if (user != null) {
            throw new AlreadyTakenException("Error: Already Taken");
        }

        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        String authToken = generateToken();

        UserData userData = new UserData(username, password, email);
        AuthData authData = new AuthData(authToken, username);

        dataAccess.addUser(userData);
        dataAccess.addAuth(authData);

        return new RegisterResult(username, authToken);
    }

    public LoginResult login(LoginRequest loginRequest) {
        return null;
    }

    public void logout() {
    }

}
