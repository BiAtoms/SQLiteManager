package com.sqlitemanager.DbPackModels;

/**
 * Created by aslan on 6/3/2017.
 */

public class PrimaryKeyModel extends ConstraintModel {


    public String name;


    public PrimaryKeyModel() {
        isInline = true;
    }


    @Override
    public String getConstraintKeyword() {
        return "PRIMARY KEY";
    }
}
