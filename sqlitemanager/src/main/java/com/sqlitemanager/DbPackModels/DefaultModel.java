package com.sqlitemanager.DbPackModels;

import com.sqlitemanager.SQLiteTypes;

/**
 * Created by aslan on 7/19/2017.
 */

public class DefaultModel extends ConstraintModel {

    public String defaultValue;
    public String dataType;

    @Override
    public String getConstraintKeyword() {
        String keywordMark = "";

        if (dataType.equals(SQLiteTypes.STRING.getJavaType()) || dataType.equals(SQLiteTypes.CHAR.getJavaType())) {
            keywordMark = "'";
        }
        return "DEFAULT " + keywordMark + defaultValue + keywordMark + "";
    }
}
