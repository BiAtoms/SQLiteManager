package com.demoapp.Models;

import com.sqlitemanager.AbstractTableModel;
import com.sqlitemanager.Annotations.Column;
import com.sqlitemanager.Annotations.Default;
import com.sqlitemanager.Annotations.NotNull;
import com.sqlitemanager.Annotations.PrimaryKey;
import com.sqlitemanager.Annotations.Unique;
import com.sqlitemanager.SQLiteManager;
import com.sqlitemanager.Tableable;

/**
 * Created by aslan on 8/10/2017.
 */
// By default, its name will be "SmthModel" table
public class SmthModel  extends AbstractTableModel {

    @Column
    @PrimaryKey
    int id;

    @Column("my_data")
    @Unique
    @NotNull
    String smth;

    @Column
    @Default("my special data")
    String my_ref;

    @Column
    @Default("3") //Todo: Default for other datatypes does not work! Only for Strings
     int asda;

}
