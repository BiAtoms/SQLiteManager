package com.demoapp.Models;

import com.sqlitemanager.Annotations.Column;
import com.sqlitemanager.Annotations.Default;
import com.sqlitemanager.Annotations.ForeignKey;
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

@TableName("drivers")
public class DriverModel implements Tableable {

    @Column
    @PrimaryKey
    public int id;

    @Column
    @Default("12-03-2017")
    public String birtdate;

    @Column
    @Unique
    public String fullname;

    @Column
    @Unique
    @NotNull
    public String email;

    @Column
    @ForeignKey(refTableName = "cars", refColumnName = "id")
    public int carId;

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
