package server;

import com.google.gson.Gson;

import java.util.HashMap;

public class ResponseException extends Exception {

    final private String code;

    public ResponseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public static ResponseException fromJson(String json) {
        var map = new Gson().fromJson(json, HashMap.class);
        String status = map.get("status").toString();
        String message = map.get("message").toString();
        return new ResponseException(status, message);
    }
}
