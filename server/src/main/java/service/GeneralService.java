package service;

import dataaccess.DataAccess;

public class GeneralService {

    private final DataAccess dataAccess;

    public GeneralService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() {
    }
}
