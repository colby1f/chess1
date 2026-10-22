package dataaccess;

import model.*;

public interface DataAccess {
    UserData getUser(String username);
    void addUser(UserData userData);
    void addAuth(AuthData authData);
    void clear();

}
