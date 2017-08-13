package com.sqlitemanager.DbPackModels;

/**
 * Created by aslan on 7/19/2017.
 */

public class DefaultModel extends ConstraintModel {

    public String defaultValue;
    public String dataType;

    @Override
    public String getConstraintKeyword() {
        String keywordMark = "";

        if (dataType.equals("String") || dataType.equals("char")) {
            keywordMark = "'";
        }
        return "DEFAULT " + keywordMark + defaultValue + keywordMark + "";
    }
}
