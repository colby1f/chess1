package service;

import dataaccess.*;

public class GeneralService {

    private final DataAccess dataAccess;

    public GeneralService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() {
    }
}
