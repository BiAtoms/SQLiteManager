package com.sqlitemanager;

/**
 * Created by aslan on 8/10/2017.
 */

public class AbstractTableModel implements Tableable {

    public long insert() {
        return SQLiteManager.getInstance().insert(this);
    }

    public long update() {
        return -1;
    }

    public long delete() {
        return -1;
    }

    @Override
    public void fill(Integer id) {
        SQLiteManager.find(this, id);
    }
}
