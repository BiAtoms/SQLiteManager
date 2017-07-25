package com.sqlitemanager.Exceptions;

/**
 * Created by aslan on 7/20/2017.
 */

public class UnknownDatatypeException extends RuntimeException {

    public UnknownDatatypeException() {
        super();
    }

    public UnknownDatatypeException(String msg) {
        super(msg);
    }
}
