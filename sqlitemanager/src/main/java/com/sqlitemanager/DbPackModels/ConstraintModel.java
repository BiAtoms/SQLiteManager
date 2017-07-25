package com.sqlitemanager.DbPackModels;

/**
 * Created by aslan on 6/1/2017.
 */

public abstract class ConstraintModel {
    public boolean isInline = false;
    public String sourceColumnName;

    public abstract String getConstraintKeyword();
}
