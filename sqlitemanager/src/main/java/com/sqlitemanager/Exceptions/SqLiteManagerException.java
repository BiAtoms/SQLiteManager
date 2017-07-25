package com.sqlitemanager.Exceptions;

import com.sqlitemanager.SQLiteManager;

/**
 * Created by aslan on 7/20/2017.
 */

public class SqLiteManagerException extends RuntimeException {
    public SqLiteManagerException() {
        super();
    }

    public SqLiteManagerException(String msg) {
        super(msg);
    }
}
