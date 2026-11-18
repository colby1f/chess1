package client;

import com.google.gson.Gson;

import java.util.HashMap;

public class ResponseException extends Exception {

    final private int code;

    public ResponseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }


    public static ResponseException fromJson(String json, int code) {
        var map = new Gson().fromJson(json, HashMap.class);
        String message = map.get("message").toString();
        return new ResponseException(code, message);
    }
}
