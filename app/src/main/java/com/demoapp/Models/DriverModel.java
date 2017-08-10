package com.demoapp.Models;

import com.sqlitemanager.AbstractTableModel;
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
public class DriverModel  extends AbstractTableModel {

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

    @Column("carId")
    @ForeignKey
    public CarModel carModel;

    //Todo: We need to add a feature to Foreign Key. If the original data
    //Todo: is updated, then the data referencing to it should be updated too.
    //Todo: And also this functionality should be able to turned on and off.

}
