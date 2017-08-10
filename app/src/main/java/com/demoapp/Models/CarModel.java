package com.demoapp.Models;

import com.sqlitemanager.Annotations.Column;
import com.sqlitemanager.Annotations.NotNull;
import com.sqlitemanager.Annotations.PrimaryKey;
import com.sqlitemanager.Annotations.TableName;
import com.sqlitemanager.Annotations.Unique;
import com.sqlitemanager.SQLiteManager;
import com.sqlitemanager.Tableable;

/**
 * Created by aslan on 7/13/2017.
 */

@TableName("cars")
public class CarModel implements Tableable {

    @Column()
    @PrimaryKey
    public int id;


    @Column("release_date")
    public String releaseDate;

    @Column()
    public String model;

    @Column()
    @Unique
    @NotNull
    public String name;

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
