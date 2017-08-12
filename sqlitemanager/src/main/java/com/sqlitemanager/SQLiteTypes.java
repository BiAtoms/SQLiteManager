package com.sqlitemanager;

/**
 * Created by aslan on 7/11/2017.
 */

public enum SQLiteTypes {

    //Todo: remove needless and unused code
    INTEGER("int", "INTEGER", int.class),
    INTEGER_NULLABLE("Integer", "INTEGER", Integer.class),
    STRING("String", "TEXT", String.class),
    DOUBLE("double", "DECIMAL(10, 5)", double.class),
    DOUBLE_NULLABLE("Double", "DECIMAL(10, 5)", Double.class),
    FLOAT("float", "DECIMAL(10, 5)", float.class),
    FLOAT_NULLABLE("Float", "DECIMAL(10, 5)", Float.class),
    SHORT("short", "INTEGER", short.class),
    SHORT_NULLABLE("Short", "INTEGER", Short.class),
    LONG("long", "INTEGER", long.class),
    LONG_NULLABLE("Long", "INTEGER", Long.class),
    CHAR("char", "TEXT", char.class),
    BYTE("byte", "BLOB", byte.class),
    BYTE_NULLABLE("Byte", "BLOB", Byte.class),
    BOOLEAN("boolean", "INTEGER", boolean.class),
    BOOLEAN_NULLABLE("Boolean", "INTEGER", Boolean.class);

    private String javaType;
    private String SQLiteType;
    private Class typeClass;

    private SQLiteTypes(String type, String SQLiteType, Class typeClass) {
        this.javaType = type;
        this.SQLiteType = SQLiteType;
        this.typeClass = typeClass;
    }

    public String getJavaType() {
        return this.javaType;
    }


    public String getSQLiteType() {
        return this.SQLiteType;
    }

    public Class getTypeClass() {
        return this.typeClass;
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
