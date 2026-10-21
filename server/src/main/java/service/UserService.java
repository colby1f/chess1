package service;

import dataaccess.DataAccess;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        return null;
    }

    public LoginResult login(LoginRequest loginRequest) {
        return null;
    }

    public void logout() {
    }
}
