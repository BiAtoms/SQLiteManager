package com.sqlitemanager.DbPackModels;

/**
 * Created by aslan on 6/3/2017.
 */

public class ForeignKeyModel extends ConstraintModel {

    public String refTableName;
    public String refColumnName;

    public String name;

    @Override
    public String getConstraintKeyword() {
        return "FOREIGN KEY (" + sourceColumnName + ") REFERENCES " + refTableName+ "("+ refColumnName+")" ;
    }
}
