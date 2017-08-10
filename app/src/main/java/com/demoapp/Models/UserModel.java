package com.demoapp.Models;

import com.sqlitemanager.Annotations.Column;
import com.sqlitemanager.Annotations.Default;
import com.sqlitemanager.Annotations.PrimaryKey;
import com.sqlitemanager.Annotations.TableName;
import com.sqlitemanager.SQLiteManager;
import com.sqlitemanager.Tableable;

/**
 * Created by aslan on 7/19/2017.
 */

@TableName("users")
public class UserModel implements Tableable {

    public final static String thisIsAName = "All_Users";

    @Column
    @PrimaryKey
    public int id;

    @Column
    @Default("Amiraslan")
    public String fullname;

    @Column("avatar_url")
    @Default("http://www.amiraslan.com/images/my_picture.png")
    public String profilePicture;

    @Column
    @Default("31")
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
