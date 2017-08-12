package com.sqlitemanager;

import android.database.Cursor;

import java.lang.reflect.Field;

/**
 * Created by aslan on 8/12/2017.
 */

abstract class AbstractDefaultCase {
    public abstract void onDefault(Field field, int indexx, Cursor cursor);
}
