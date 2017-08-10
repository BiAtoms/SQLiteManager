package com.demoapp.Models;

import com.sqlitemanager.AbstractTableModel;
import com.sqlitemanager.Annotations.Column;
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
public class CityModel  extends AbstractTableModel {

    @Column
    @PrimaryKey
    public int id;

    @Column
    @NotNull
    @Unique
    public String name;

    @Column
    @NotNull
    public int num_of_roads;

    @Column("additional_info")
    public String additionalInfo;

}
