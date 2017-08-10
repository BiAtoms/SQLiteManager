package com.sqlitemanager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sqlitemanager.Annotations.ColumnName;
import com.sqlitemanager.Annotations.Default;
import com.sqlitemanager.Annotations.ForeignKey;
import com.sqlitemanager.Annotations.NotColumn;
import com.sqlitemanager.Annotations.NotNull;
import com.sqlitemanager.Annotations.PrimaryKey;
import com.sqlitemanager.Annotations.TableName;
import com.sqlitemanager.Annotations.Unique;
import com.sqlitemanager.DbPackModels.ColumnModel;
import com.sqlitemanager.DbPackModels.ColumnNameModel;
import com.sqlitemanager.DbPackModels.ConstraintModel;
import com.sqlitemanager.DbPackModels.DefaultModel;
import com.sqlitemanager.DbPackModels.ForeignKeyModel;
import com.sqlitemanager.DbPackModels.NotNullModel;
import com.sqlitemanager.DbPackModels.PrimaryKeyModel;
import com.sqlitemanager.DbPackModels.TableModel;
import com.sqlitemanager.DbPackModels.UniqueModel;
import com.sqlitemanager.Exceptions.UnknownDatatypeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * Created by aslan on 5/10/2017.
 */

public class SQLiteManager extends SQLiteOpenHelper {

    protected final static String TAG = "SQLiteManager";

    private static SQLiteManager sqLiteManager = null;

    //TODO: Replace ArrayLists with Arrays or Lists as much as possible for the sake of efficiency;

    private ArrayList<Class> tableTypesList;

    private ArrayList<TableModel> databaseTables = new ArrayList<>();

    private ArrayList<String> createdTableQueries = new ArrayList<>();

    private ArrayList<String> tablesNames = new ArrayList<>();

    private Context context;

    private boolean willBeUpdated;

    private SQLiteManager(Builder builder) {
        super(builder.context, builder.databaseName, null, builder.databaseVersion);
        Log.i("Database operations", "Database created or opened...");
        tableTypesList = builder.classes;
        context = builder.context;
        willBeUpdated = builder.willBeUpdated;
        findAllAnnotatedFields();
    }

    public static SQLiteManager getInstance() {
        if (sqLiteManager != null) {
            return sqLiteManager;
        }
        throw new RuntimeException("SQLiteManager has not been initialized yet. SQLiteManager is null.");
    }

    public static ArrayList<TableModel> getAllTables() {
        return sqLiteManager.databaseTables;
    }

    public static ArrayList<String> getAllTableNames() {
        if (sqLiteManager.tablesNames.size() == 0) {
            for (TableModel table : sqLiteManager.databaseTables) {
                sqLiteManager.tablesNames.add(table.tableName);
            }
        }
        return sqLiteManager.tablesNames;
    }

    ArrayList<Class> getTableTypesList() {
        return tableTypesList;
    }

    public static int getNumberOfTables() {
        return sqLiteManager.databaseTables.size();
    }

    private void prepareCreateTablesQueryStr() {

        String query;
        createdTableQueries.clear();

        //TODO: Not sure if it is going to work... Check it
        String updateText = "IF NOT EXISTS";
        if (willBeUpdated) updateText = "";

        for (TableModel databaseTable : databaseTables) {
            query = "CREATE TABLE " + updateText + " ";
            query += databaseTable.tableName + " (";
            int index = 0;
            for (ColumnModel tableColumnModel : databaseTable.columnModels) {
                index++;
                query += tableColumnModel.name + " " + SQLiteTypes.findType(tableColumnModel.datatype);
                if (tableColumnModel.hasInlineConstraint) {
                    for (ConstraintModel constraint : tableColumnModel.constraintModels) {
                        query += " " + constraint.getConstraintKeyword();
                    }
                }
                query += (index == databaseTable.columnModels.size()) ? "" : ", ";
            }
            ArrayList<ConstraintModel> tableConstraints = databaseTable.getConstraintModels();
            for (ConstraintModel constraintModel : tableConstraints) {
                query += ", " + constraintModel.getConstraintKeyword();
            }
            query += ");";
            createdTableQueries.add(query);
        }
    }

    private void findAllAnnotatedFields() {
        //TODO: Don't hard code anything! ("value")
        String columnNameAnnotation = "value";

        databaseTables.clear();

        for (Class tableClass : tableTypesList) {
            // SortOrder the arrays and ArrayLists which is achieved by Reflection.
            // Because it takes the element in no specific order.
            // There is a possibility that it can take them in different order.
            // So, first sort, then use them. Just to be on the safe side!
            Field[] fields = tableClass.getFields();
            Arrays.sort(fields, new Comparator<Field>() {
                @Override
                public int compare(Field field, Field t1) {
                    return field.getName().compareTo(t1.getName());
                }
            });

            TableModel table = null;

            Annotation tableNameAnnotation = tableClass.getAnnotation(TableName.class);
            if (tableNameAnnotation != null) {
                String currentTableName = ((TableName) tableNameAnnotation).value();
                table = new TableModel(currentTableName);
            } else {
                table = new TableModel(tableClass.getSimpleName());
            }

            for (Field column : fields) {
                Annotation notColumn = column.getAnnotation(NotColumn.class);
                Annotation primaryKey = column.getAnnotation(PrimaryKey.class);
                Annotation foreignKey = column.getAnnotation(ForeignKey.class);
                Annotation unique = column.getAnnotation(Unique.class);
                Annotation columnName = column.getAnnotation(ColumnName.class);
                Annotation notNull = column.getAnnotation(NotNull.class);
                Annotation _default = column.getAnnotation(Default.class);

                if (notColumn != null) {
                    continue;
                }

                ColumnModel currentColumnModel;
                ColumnNameModel columnNameModel = new ColumnNameModel();
                columnNameModel.columnName = column.getName();

                if (columnName != null) {
                    columnNameModel.columnName = ((ColumnName) columnName).value();
                }

                currentColumnModel = new ColumnModel(columnNameModel.columnName, column.getType().getSimpleName());
                currentColumnModel.hasInlineConstraint = false;

                if (primaryKey != null) {
                    PrimaryKeyModel primaryKeyModel = new PrimaryKeyModel();
                    primaryKeyModel.sourceColumnName = columnNameModel.columnName;
                    currentColumnModel.hasInlineConstraint = true;
                    primaryKeyModel.isInline = true;
                    currentColumnModel.constraintModels.add(primaryKeyModel);
                }

                if (foreignKey != null) {
                    ForeignKeyModel foreignKeyModel = new ForeignKeyModel();
                    foreignKeyModel.sourceColumnName = columnNameModel.columnName;
                    foreignKeyModel.refTableName = ((ForeignKey) foreignKey).refTableName();
                    foreignKeyModel.refColumnName = ((ForeignKey) foreignKey).refColumnName();
                    foreignKeyModel.isInline = false;
                    table.addConstraint(foreignKeyModel);
                }

                if (unique != null) {
                    UniqueModel uniqueModel = new UniqueModel();
                    uniqueModel.sourceColumnName = columnNameModel.columnName;
                    currentColumnModel.hasInlineConstraint = true;
                    uniqueModel.isInline = true;
                    currentColumnModel.constraintModels.add(uniqueModel);
                }

                if (_default != null) {
                    DefaultModel defaultModel = new DefaultModel();
                    defaultModel.sourceColumnName = columnNameModel.columnName;
                    defaultModel.defaultValue = ((Default) _default).value();
                    defaultModel.dataType = column.getType().getSimpleName();
                    currentColumnModel.hasInlineConstraint = true;
                    defaultModel.isInline = true;
                    currentColumnModel.constraintModels.add(defaultModel);
                }

                if (notNull != null) {
                    NotNullModel notNullModel = new NotNullModel();
                    notNullModel.sourceColumnName = columnNameModel.columnName;
                    currentColumnModel.hasInlineConstraint = true;
                    notNullModel.isInline = true;
                    currentColumnModel.constraintModels.add(notNullModel);
                }
                table.columnModels.add(currentColumnModel);
            }
            databaseTables.add(table);
        }
// showAllTablesAndColumns();
//This part had been written to slide the tables down which have more constraints.
//        Collections.sort(databaseTables, new Comparator<TableModel>() {
//            @Override
//            public int compare(TableModel t1, TableModel t2) {
//                return String.valueOf(t1.getConstraintModels().size()).compareTo(String.valueOf(t2.getConstraintModels().size()));  //.getName().compareToIgnoreCase(s2.getName());
//            }
//        });
        prepareCreateTablesQueryStr();
    }


    public <T extends Tableable> long insert(T tableModel) {
        String name = Utils.getTableName(tableModel.getClass());

        ContentValues contentValues = new ContentValues();

        Field[] allFields = tableModel.getClass().getFields();

        Arrays.sort(allFields, new Comparator<Field>() {
            @Override
            public int compare(Field field, Field t1) {
                return field.getName().compareTo(t1.getName());
            }
        });

        for (Field field : allFields) {
            try {
                if (field.getAnnotation(NotColumn.class) != null) continue;

                switch (field.getType().getSimpleName()) {
                    case "int":
                        if (field.getAnnotation(PrimaryKey.class) != null && ((int) field.get(tableModel)) == 0) {
                            break;
                        }
                        if (field.getAnnotation(Default.class) != null && field.get(tableModel) == null)
                            break;

                        contentValues.put(Utils.getMemberColumnName(field), (int) field.get(tableModel));
                        break;
                    case "String":
                        if (field.getAnnotation(Default.class) != null && field.get(tableModel) == null)
                            break;
                        contentValues.put(Utils.getMemberColumnName(field), (String) field.get(tableModel));
                        break;
                    case "double":
                        if (field.getAnnotation(Default.class) != null && field.get(tableModel) == null)
                            break;
                        contentValues.put(Utils.getMemberColumnName(field), (double) field.get(tableModel));
                        break;
                    case "float":
                        if (field.getAnnotation(Default.class) != null && field.get(tableModel) == null)
                            break;
                        contentValues.put(Utils.getMemberColumnName(field), (float) field.get(tableModel));
                        break;
                    case "short":
                        if (field.getAnnotation(Default.class) != null && field.get(tableModel) == null)
                            break;
                        contentValues.put(Utils.getMemberColumnName(field), (short) field.get(tableModel));
                        break;
                    case "long":
                        if (field.getAnnotation(Default.class) != null && field.get(tableModel) == null)
                            break;
                        contentValues.put(Utils.getMemberColumnName(field), (long) field.get(tableModel));
                        break;
                    case "byte":
                        if (field.getAnnotation(Default.class) != null && field.get(tableModel) == null)
                            break;
                        contentValues.put(Utils.getMemberColumnName(field), (byte) field.get(tableModel));
                        break;
                    case "boolean":
                        if (field.getAnnotation(Default.class) != null && field.get(tableModel) == null)
                            break;
                        contentValues.put(Utils.getMemberColumnName(field), (boolean) field.get(tableModel));
                        break;
                    default:
                        throw new UnknownDatatypeException(TAG + ": " + field.getType().getSimpleName() + " is not supported. Unknown type from Cursor!");
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        try {
            return sqLiteManager.getWritableDatabase().insertWithOnConflict(name, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        } catch (Exception e) {
            return SqlResponse.TableNotExist.getCode();
        }
    }


    public <T extends Tableable> SqlResponse delete(T tableModel) {
        String tableName = Utils.getTableName(tableModel.getClass());
        sqLiteManager.getWritableDatabase().delete(tableName, "?=?", new String[]{"1", "jack"});
        return SqlResponse.Successful;
    }


    //TODO: Complete this
    private String getStringForeignKey(String columnWithForeignKey, String mainTableName) {
        //return mainTableName + " INNER JOIN " + referenceTableName + " ON " + referenceTableName + "." + referenceColumn + "=" + mainTableName + "." + columnWithForeignKey;
        return "";
    }

    @SuppressWarnings("unchecked")
    public static <T extends Tableable> ArrayList<T> all(String tableName) {
        return sqLiteManager.selectAll(tableName, Utils.getTableClass(tableName, sqLiteManager.tableTypesList), null, null, null, null, null, null);
    }

    public static <T extends Tableable> ArrayList<T> all(Class<T> modelClass) {
        return sqLiteManager.selectAll(Utils.getTableName(modelClass), modelClass, null, null, null, null, null, null);
    }

    private <T extends Tableable> ArrayList<T> selectAll(String name, Class<T> modelClass, String[] args,
                                                         String condition, SortOrder sortOrder,
                                                         Integer limit, String columnWithForeignKey, String[] columns) {
        Cursor cursor;

        SQLiteDatabase sqLiteDatabase = sqLiteManager.getReadableDatabase();


        /**
         *      //TODO: Add this to make Foreign key property enabled.
         *   Cursor cursor = db.query(
         NoteContract.Note.TABLE_NAME + " LEFT OUTER JOIN authors ON notes._id=authors.note_id",
         projection,
         selection,
         selectionArgs,
         null,
         null,
         "notes._id");
         */

        String foreignKey = getStringForeignKey(columnWithForeignKey, name);

        cursor = sqLiteDatabase.query(
                name + foreignKey,
                getColumnsForSelect(sqLiteDatabase, name, columns),
                condition,
                args,
                null,
                null,
                null
        );

        ArrayList<T> tableModels = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                T tableModel;
                try {
                    tableModel = modelClass.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Not successfully instantiated:" + " " + e.getMessage());
                }

                Field[] fields = tableModel.getClass().getFields();
                int index = 0;

                for (Field field : fields) {
                    if (field.getAnnotation(NotColumn.class) != null) continue;
                    try {
                        switch (field.getType().getSimpleName()) {
                            case "int":
                                field.set(tableModel, cursor.getInt(index++));
                                break;
                            case "String":
                                field.set(tableModel, cursor.getString(index++));
                                break;
                            case "double":
                                field.set(tableModel, cursor.getDouble(index++));
                                break;
                            case "float":
                                field.set(tableModel, cursor.getFloat(index++));
                                break;
                            case "short":
                                field.set(tableModel, cursor.getShort(index++));
                                break;
                            case "long":
                                field.set(tableModel, cursor.getLong(index++));
                                break;
                            case "byte":
                                field.set(tableModel, cursor.getBlob(index++));
                                break;
                            case "boolean":
                                field.set(tableModel, cursor.getInt(index++));
                                break;
                            default:
                                throw new UnknownDatatypeException(TAG + ": " + field.getType().getSimpleName() + " is not supported. Unknown type from Cursor!");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                tableModels.add(tableModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tableModels;
    }


    public static SqlResponse deleteTable(String tableName) {
        if (Utils.tableExist(tableName))
            sqLiteManager.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + tableName);
        else
            return SqlResponse.TableNotExist;

        return SqlResponse.Successful;
    }

    public static <T extends Tableable> SqlResponse deleteTable(Class<T> tableClass) {
        return deleteTable(Utils.getTableName(tableClass));
    }


    public static SqlResponse clearTableData(String tableName) {
        long result;
        if (Utils.tableExist(tableName))
            result = sqLiteManager.getWritableDatabase().delete(tableName, "1=1", null);
        else
            return SqlResponse.TableNotExist;

        if (result > 0)
            return SqlResponse.Successful;
        return SqlResponse.Failed;
    }

    public static <T extends Tableable> SqlResponse clearTableData(Class<T> tableClass) {
        return clearTableData(Utils.getTableName(tableClass));
    }


    private String[] getColumnsForSelect(SQLiteDatabase sqLiteDatabase, String name, String[] columns) {
        if (columns != null && columns.length != 0) {
            return columns;
        }

        Cursor dbCursor;

        try {
            dbCursor = sqLiteDatabase.query(name, null, null, null, null, null, null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        String[] projections = dbCursor.getColumnNames();

        dbCursor.close();

        return projections;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String query : createdTableQueries) {
            db.execSQL(query);
        }
        Log.i(TAG, "Database created! All tables are ready!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        for (TableModel databaseTable : databaseTables) {
            //Drops old tables
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + databaseTable.tableName);
        }
        Log.i(TAG, "All tables dropped! Database is empty ");

        //Creates new tables
        onCreate(sqLiteDatabase);
        Log.i(TAG, "Database is successfully upgraded");
    }


    //TODO: Name this as QueryBuilder
    public static class Select {

        private Class tableClass;
        private String tableName;
        private String[] args;
        private String condition;
        private SortOrder sortOrder = SortOrder.ASC;
        private int limit = -1;
        private String[] columns;
        private String columnWithForeignKey;

        public Select(String tableName) {
            this.tableName = tableName;
            tableClass = Utils.getTableClass(tableName, sqLiteManager.tableTypesList);
        }

        public <T extends Tableable> Select(Class<T> tableClass) {
            this.tableClass = tableClass;
        }

        @SuppressWarnings("unchecked")
        public <T extends Tableable> ArrayList<T> get() {
            return sqLiteManager.selectAll(tableName, tableClass, args,
                    condition, sortOrder, limit, columnWithForeignKey, columns);
        }

        public Select where(String condition, String... args) {
            this.condition = condition;
            List<String> list = new ArrayList<>(Arrays.asList(args));
            this.args = list.toArray(new String[list.size()]);
            return this;
        }

        public Select sort(SortOrder order) {
            this.sortOrder = order;
            return this;
        }

        public Select columns(String... columns) {
            List<String> list = new ArrayList<>(Arrays.asList(columns));
            this.columns = list.toArray(new String[list.size()]);
            return this;
        }

        public Select limit(int limit) {
            this.limit = limit;
            return this;
        }

        //TODO: Complete these limit method properly
        public Select limit(int from, int to) {
            this.limit = to;
            return this;
        }

        public Select innerJoin(String columnName) {
            this.columnWithForeignKey = columnName;
            return this;
        }
    }

    public static class Builder {
        private Context context;
        private int databaseVersion = 1;
        private ArrayList<Class> classes = null;
        private boolean willBeUpdated = false;
        private String databaseName = "EXAMPLE.DB";

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTableList(Class... classes) {
            this.classes = new ArrayList<>(Arrays.asList(classes));
            return this;
        }

        public Builder setDBVersion(int version) {
            this.databaseVersion = version;
            return this;
        }

        public Builder setDBName(String databaseName) {
            //TODO: Check in Debug (to lower case)
            if (databaseName.toLowerCase().endsWith(".db"))
                this.databaseName = databaseName;
            else {
                this.databaseName = databaseName + ".DB";
            }
            return this;
        }

        public SQLiteManager buildDatabase() {
            if (sqLiteManager != null)
                throw new RuntimeException("SQLiteManager has already been initialized once");
            sqLiteManager = new SQLiteManager(this);
            return sqLiteManager;
        }

        public Builder setWillBeUpdated(boolean willBeUpdated) {
            this.willBeUpdated = willBeUpdated;
            return this;
        }
    }

}