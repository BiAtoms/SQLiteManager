package com.sqlitemanager;

/**
 * Created by aslan on 7/11/2017.
 */

public enum SQLiteTypes {
    INT("int", "INTEGER"),
    STRING("String", "TEXT"),
    DOUBLE("double", "DECIMAL(10, 5)"),
    FLOAT("float", "DECIMAL(10, 5)"),
    SHORT("short", "INTEGER"),
    LONG("long", "INTEGER"),
    CHAR("char", "TEXT"),
    BYTE("byte", "BLOB"),
    BOOLEAN("boolean", "INTEGER");

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
