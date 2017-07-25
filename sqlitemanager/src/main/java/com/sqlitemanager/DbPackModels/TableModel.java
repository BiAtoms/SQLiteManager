package com.sqlitemanager.DbPackModels;

import java.util.ArrayList;

/**
 * Created by aslan on 6/1/2017.
 */

public class TableModel {
    public String tableName;
    public ArrayList<ColumnModel> columnModels;
    private ArrayList<ConstraintModel> constraintModels;

    public void addConstraint(ConstraintModel constraintModel) {
        constraintModels.add(constraintModel);
    }

    public ArrayList<ConstraintModel> getConstraintModels() {
        return constraintModels;
    }

    private TableModel() {
        columnModels = new ArrayList<>();
        constraintModels = new ArrayList<>();
    }

    public TableModel(String tableName) {
        this();
        this.tableName = tableName;
    }

    public String findConstraint(String columnName) {
        for (ConstraintModel constraintModel : constraintModels) {
            if (constraintModel.sourceColumnName.equals(columnName))
                return constraintModel.getConstraintKeyword();
        }
        return "";
    }

    public ArrayList<ConstraintModel> getRegularConstraints() {
        ArrayList<ConstraintModel> arrayList = new ArrayList<>();
        for (ConstraintModel constraintModel : constraintModels) {
            if (!constraintModel.isInline)
                arrayList.add(constraintModel);
        }
        return arrayList;
    }
}
