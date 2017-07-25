package com.demoapp.Models;

import com.sqlitemanager.Annotations.NotColumn;
import com.sqlitemanager.Annotations.NotNull;
import com.sqlitemanager.Annotations.PrimaryKey;
import com.sqlitemanager.Annotations.TableName;
import com.sqlitemanager.Annotations.Unique;
import com.sqlitemanager.SQLiteManager;
import com.sqlitemanager.Tableable;
import com.sqlitemanager.Utils;

/**
 * Created by aslan on 7/13/2017.
 */

@TableName("cities")
public class CityModel implements Tableable {


    @PrimaryKey
    public int id;

    @NotNull
    @Unique
    public String name;

    @NotNull
    public int num_of_roads;

    @NotColumn
    public String additionalInfo;

    @Override
    public long insert() {
        return SQLiteManager.getInstance().insert(this);
    }

    @Override
    public long update() {
        return 0;
    }

    @Override
    public long delete() {
        return 0;
    }
}
