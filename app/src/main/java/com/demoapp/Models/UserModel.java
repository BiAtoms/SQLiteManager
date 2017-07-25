package com.demoapp.Models;

import com.sqlitemanager.Annotations.ColumnName;
import com.sqlitemanager.Annotations.Default;
import com.sqlitemanager.Annotations.NotColumn;
import com.sqlitemanager.Annotations.PrimaryKey;
import com.sqlitemanager.Annotations.TableName;
import com.sqlitemanager.SQLiteManager;
import com.sqlitemanager.Tableable;
import com.sqlitemanager.Utils;

/**
 * Created by aslan on 7/19/2017.
 */

@TableName("users")
public class UserModel implements Tableable {

    @NotColumn
    public final static String thisIsAName = "All_Users";

    @PrimaryKey
    public int id;

    @Default("Amiraslan")
    public String fullname;

    @ColumnName("avatar_url")
    @Default("http://www.amiraslan.com/images/my_picture.png")
    public String profilePicture;

    public int age;

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
