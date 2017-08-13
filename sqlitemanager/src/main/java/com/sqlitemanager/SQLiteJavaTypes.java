package com.sqlitemanager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aslan on 8/13/2017.
 */

public class SQLiteJavaTypes {
    private Map<String, String> mapOfTypes;

    public SQLiteJavaTypes() {
        mapOfTypes = new HashMap<String, String>();
        mapOfTypes.put("int", "INTEGER");
        mapOfTypes.put("Integer", "INTEGER");
        mapOfTypes.put("String", "TEXT");
        mapOfTypes.put("double", "DECIMAL(10, 5)");
        mapOfTypes.put("Double", "DECIMAL(10, 5)");
        mapOfTypes.put("float", "DECIMAL(10, 5)");
        mapOfTypes.put("Float", "DECIMAL(10, 5)");
        mapOfTypes.put("short", "INTEGER");
        mapOfTypes.put("Short", "INTEGER");
        mapOfTypes.put("long", "INTEGER");
        mapOfTypes.put("Long", "INTEGER");
        mapOfTypes.put("char", "TEXT");
        mapOfTypes.put("byte", "BLOB");
        mapOfTypes.put("Byte", "BLOB");
        mapOfTypes.put("boolean", "INTEGER");
        mapOfTypes.put("Boolean", "INTEGER");
    }

    public String getSQLiteType(String javaType) {
        return mapOfTypes.get(javaType);
    }

}
