package com.sqlitemanager.DbPackModels;

/**
 * Created by aslan on 6/6/2017.
 */

public class UniqueModel extends ConstraintModel {

    public UniqueModel() {
        isInline = true;
    }

    @Override
    public String getConstraintKeyword() {
        return "UNIQUE";
    }
}
