package com.demoapp.Models;

import com.sqlitemanager.Annotations.ColumnName;
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

@TableName("cars")
public class CarModel implements Tableable {

    @PrimaryKey
    public int id;

    @ColumnName("release_date")
    public String releaseDate;

    public String model;

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

    //TODO: Add this to UTILS
//    public static <T> String getClassName(Class<T> className)
//    {
//        TableName annotation = className.getAnnotation(TableName.class);
//
//        if(annotation != null)
//        return annotation.value();
//
//        return null;
//    }

}
