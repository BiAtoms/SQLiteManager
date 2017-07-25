package com.sqlitemanager.DbPackModels;

/**
 * Created by aslan on 7/18/2017.
 */

public class NotNullModel extends ConstraintModel {
    @Override
    public String getConstraintKeyword() {
        return "NOT NULL";
    }
}
