package com.sqlitemanager.DbPackModels;

import java.util.ArrayList;

/**
 * Created by aslan on 6/1/2017.
 */

public class ColumnModel {
    public String name;
    public String datatype;
    public boolean hasInlineConstraint = false;

    public ArrayList<ConstraintModel> constraintModels;

    public ColumnModel(String name, String datatype)
    {
        this.name = name;
        this.datatype = datatype;

        constraintModels = new ArrayList<>();
    }
}
