package com.sqlitemanager;

import android.database.Cursor;
import android.util.Log;

import com.sqlitemanager.Annotations.Column;
import com.sqlitemanager.Annotations.ForeignKey;
import com.sqlitemanager.Annotations.PrimaryKey;
import com.sqlitemanager.Annotations.TableName;
import com.sqlitemanager.DbPackModels.ColumnAnnotationModel;
import com.sqlitemanager.Exceptions.SqLiteManagerException;
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
        if (anno.value().equals(ColumnAnnotationModel.getDefaultValue())) return field.getName();
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

    static Field getPrimaryKeyField(Class clazz) {
        Field[] fields = clazz.getFields();

        for (Field item : fields) {
            if (item.isAnnotationPresent(PrimaryKey.class)) {
                return item;
            }
        }
        return null;
    }

    static <T extends Tableable> SqlResponse readingSwitchAction(String simpleNameOfDataType, final Field field, T tableModel, int index, Cursor cursor, AbstractDefaultCase defaultCase) {
        if (!field.isAnnotationPresent(Column.class)) return SqlResponse.Failed;


        try {
            switch (simpleNameOfDataType) {
                case "int":
                    field.set(tableModel, cursor.getInt(index));
                    break;
                case "String":
                    field.set(tableModel, cursor.getString(index));
                    break;
                case "double":
                    field.set(tableModel, cursor.getDouble(index));
                    break;
                case "float":
                    field.set(tableModel, cursor.getFloat(index));
                    break;
                case "short":
                    field.set(tableModel, cursor.getShort(index));
                    break;
                case "long":
                    field.set(tableModel, cursor.getLong(index));
                    break;
                case "byte":
                    field.set(tableModel, cursor.getBlob(index));
                    break;
                case "boolean":
                    field.set(tableModel, cursor.getInt(index));
                    break;
                case "Integer":
                    field.set(tableModel, cursor.getInt(index));
                    break;
                case "Double":
                    field.set(tableModel, cursor.getDouble(index));
                    break;
                case "Float":
                    field.set(tableModel, cursor.getFloat(index));
                    break;
                case "Short":
                    field.set(tableModel, cursor.getShort(index));
                    break;
                case "Long":
                    field.set(tableModel, cursor.getLong(index));
                    break;
                case "char":
                    field.set(tableModel, cursor.getString(index));
                    break;
                case "Byte":
                    field.set(tableModel, cursor.getBlob(index));
                    break;
                case "Boolean":
                    field.set(tableModel, cursor.getInt(index));
                    break;
                default:
                    defaultCase.onDefault(field, index, cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return SqlResponse.Successful;
    }

    static boolean isColumn(Field field) {
        return field.isAnnotationPresent(Column.class);
    }

}
