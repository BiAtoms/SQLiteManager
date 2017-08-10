package com.sqlitemanager;

import java.util.ArrayList;

/**
 * Created by aslan on 7/13/2017.
 */

public interface Tableable {
    public long insert();

    public long update();

    public long delete();
}
