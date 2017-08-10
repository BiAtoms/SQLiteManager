package com.sqlitemanager.DbPackModels;

import com.sqlitemanager.Annotations.Column;
import com.sqlitemanager.Exceptions.SqLiteManagerException;

import java.lang.reflect.Method;

/**
 * Created by aslan on 7/18/2017.
 */

public class ColumnAnnotationModel {
    public String columnName;

    public static String getDefaultValue() {
        String columnDefaultValue;
        try {
            Class<?> clazz = Column.class;
            Method defaultValueMethod = clazz.getDeclaredMethod("value");
            columnDefaultValue = (String) defaultValueMethod.getDefaultValue();
        } catch (Exception e) {
            throw new SqLiteManagerException("No value method inside Column constraint");
        }
        return columnDefaultValue;
    }
}
