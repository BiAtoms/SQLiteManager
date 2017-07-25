package com.sqlitemanager;

/**
 * Created by aslan on 7/20/2017.
 */

public enum SqlResponse {
    AlreadyExist(-20),
    TableNotExist(-30),
    Successful(-10),
    Failed(-40),
    Conflict(-50);

    private int code;

    private SqlResponse(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}
