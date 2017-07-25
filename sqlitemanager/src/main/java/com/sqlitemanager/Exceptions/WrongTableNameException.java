package com.sqlitemanager.Exceptions;

/**
 * Created by aslan on 7/20/2017.
 */

public class WrongTableNameException extends RuntimeException {

    public WrongTableNameException() {
        super();
    }

    public WrongTableNameException(String msg) {
        super(msg);
    }
}
