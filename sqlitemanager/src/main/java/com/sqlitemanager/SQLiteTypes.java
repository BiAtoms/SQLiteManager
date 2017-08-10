package com.sqlitemanager;

/**
 * Created by aslan on 7/11/2017.
 */

public enum SQLiteTypes {
    INTEGER("int", "INTEGER"),
    INTEGER_NULLABLE("Integer", "INTEGER"),
    STRING("String", "TEXT"),
    DOUBLE("double", "DECIMAL(10, 5)"),
    DOUBLE_NULLABLE("Double", "DECIMAL(10, 5)"),
    FLOAT("float", "DECIMAL(10, 5)"),
    FLOAT_NULLABLE("Float", "DECIMAL(10, 5)"),
    SHORT("short", "INTEGER"),
    SHORT_NULLABLE("Short", "INTEGER"),
    LONG("long", "INTEGER"),
    LONG_NULLABLE("Long", "INTEGER"),
    CHAR("char", "TEXT"),
    CHAR_NULLABLE("Char", "TEXT"),
    BYTE("byte", "BLOB"),
    BYTE_NULLABLE("Byte", "BLOB"),
    BOOLEAN("boolean", "INTEGER"),
    BOOLEAN_NULLABLE("Boolean", "INTEGER");

    private String javaType;
    private String SQLiteType;

    private SQLiteTypes(String type, String SQLiteType) {
        this.javaType = type;
        this.SQLiteType = SQLiteType;
    }

    public String getJavaType() {
        return this.javaType;
    }


    public String getSQLiteType() {
        return this.SQLiteType;

    }

    public static String findType(String javaType) {
        for (SQLiteTypes value : SQLiteTypes.values()) {
            if (value.getJavaType().equals(javaType)) {
                return value.getSQLiteType();
            }
        }
        return null;
    }

}
