package com.sqlitemanager;

import com.sqlitemanager.Annotations.Column;
import com.sqlitemanager.Annotations.TableName;
import com.sqlitemanager.DbPackModels.ColumnAnnotationModel;
import com.sqlitemanager.Exceptions.WrongTableNameException;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by aslan on 7/19/2017.
 */

public class Utils {
    protected final static String TAG = "Utils";

    static String getMemberColumnName(Field field) {
        Column anno = field.getAnnotation(Column.class);
        if (anno.value().equals(ColumnAnnotationModel.defaultValue)) return field.getName();
        return anno.value();
    }

    public static String getTableName(Class a) {
        TableName anno;
        try {
            anno = a.newInstance().getClass().getAnnotation(TableName.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if (anno == null) return a.getClass().getName();
        return anno.value();
    }

    public static Boolean tableExist(String tableName) {
        ArrayList names = SQLiteManager.getAllTableNames();
        return names.contains(tableName);
    }

    static Class getTableClass(String tableName, ArrayList<Class> typeList) throws WrongTableNameException {
        for (Class model : typeList) {
            if (Utils.getTableName(model).equals(tableName)) {
                return model;
            }
        }
        throw new WrongTableNameException(TAG + " Table type not found");
    }

    public static String getTableName(String className, ArrayList<Class> typeList) {
        //className += "  ";
        return className;
    }

}
