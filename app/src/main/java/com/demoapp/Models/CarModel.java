package com.demoapp.Models;

import com.sqlitemanager.AbstractTableModel;
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
public class CarModel extends AbstractTableModel{

    //Todo: Apply Encapsulation before first official release

    @Column()
    @PrimaryKey
    public int id;

    @Column("release_date")
    public String releaseDate;

    @Column
    public String model;

    @Column
    @Unique
    @NotNull
    public String name;


}
