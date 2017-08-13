package com.sqlitemanager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sqlitemanager.Annotations.Column;
import com.sqlitemanager.Annotations.Default;
import com.sqlitemanager.Annotations.ForeignKey;
import com.sqlitemanager.Annotations.NotNull;
import com.sqlitemanager.Annotations.PrimaryKey;
import com.sqlitemanager.Annotations.TableName;
import com.sqlitemanager.Annotations.Unique;
import com.sqlitemanager.DbPackModels.ColumnModel;
import com.sqlitemanager.DbPackModels.ColumnAnnotationModel;
import com.sqlitemanager.DbPackModels.ConstraintModel;
import com.sqlitemanager.DbPackModels.DefaultModel;
import com.sqlitemanager.DbPackModels.ForeignKeyModel;
import com.sqlitemanager.DbPackModels.NotNullModel;
import com.sqlitemanager.DbPackModels.PrimaryKeyModel;
import com.sqlitemanager.DbPackModels.TableModel;
import com.sqlitemanager.DbPackModels.UniqueModel;
import com.sqlitemanager.Exceptions.SqLiteManagerException;
import com.sqlitemanager.Exceptions.UnknownDatatypeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


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

    private Builder builder;

    private boolean willBeUpdated;

    private SQLiteManager(Builder builder) {
        super(builder.context, builder.databaseName, null, builder.databaseVersion);
        Log.i("Database operations", "Database created or opened...");
        this.builder = builder;
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
                query += tableColumnModel.name + " " + new SQLiteJavaTypes().getSQLiteType(tableColumnModel.datatype);
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

    public static void deleteDatabase() {
        sqLiteManager.context.deleteDatabase(sqLiteManager.getDatabaseName());
    }

    public static void refreshDatabase() {
        deleteDatabase();
        sqLiteManager = new SQLiteManager(sqLiteManager.builder);
    }

    private void findAllAnnotatedFields() {
        String columnDefaultValue;
        try {
            Class<?> clazz = Column.class;
            Method defaultValueMethod = clazz.getDeclaredMethod("value");
            columnDefaultValue = (String) defaultValueMethod.getDefaultValue();
        } catch (Exception e) {
            throw new SqLiteManagerException("No value method inside Column constraint");
        }

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

            TableModel table;

            Annotation tableNameAnnotation = tableClass.getAnnotation(TableName.class);
            if (tableNameAnnotation != null) {
                String currentTableName = ((TableName) tableNameAnnotation).value();
                table = new TableModel(currentTableName);
            } else {
                table = new TableModel(tableClass.getSimpleName());
            }

            for (Field column : fields) {

                if (!column.isAnnotationPresent(Column.class)) continue;

                Annotation primaryKey = column.getAnnotation(PrimaryKey.class);
                Annotation foreignKey = column.getAnnotation(ForeignKey.class);
                Annotation unique = column.getAnnotation(Unique.class);
                Annotation columnName = column.getAnnotation(Column.class);
                Annotation notNull = column.getAnnotation(NotNull.class);
                Annotation _default = column.getAnnotation(Default.class);

                ColumnModel currentColumnModel;
                ColumnAnnotationModel columnAnnotationModel = new ColumnAnnotationModel();
                columnAnnotationModel.columnName = column.getName();

                if (!((Column) columnName).value().equals(columnDefaultValue)) {
                    columnAnnotationModel.columnName = ((Column) columnName).value();
                }
                String simpleNameOfDataType = (foreignKey != null) ? "Integer" : column.getType().getSimpleName();
                currentColumnModel = new ColumnModel(columnAnnotationModel.columnName, simpleNameOfDataType);
                currentColumnModel.hasInlineConstraint = false;

                if (primaryKey != null) {
                    PrimaryKeyModel primaryKeyModel = new PrimaryKeyModel();
                    primaryKeyModel.sourceColumnName = columnAnnotationModel.columnName;
                    currentColumnModel.hasInlineConstraint = true;
                    primaryKeyModel.isInline = true;
                    currentColumnModel.constraintModels.add(primaryKeyModel);
                }

                if (foreignKey != null) {
                    ForeignKeyModel foreignKeyModel = new ForeignKeyModel();
                    foreignKeyModel.sourceColumnName = columnAnnotationModel.columnName;

                    Field primaryKeyField = Utils.getPrimaryKeyField(column.getType());
                    if (primaryKeyField == null) {
                        throw new SqLiteManagerException("Referenced table " + column.getType().getSimpleName() + " does not have primary key");
                    }
                    foreignKeyModel.refColumnName = primaryKeyField.getName();

                    if (column.getType().isAnnotationPresent(TableName.class)) {
                        foreignKeyModel.refTableName = column.getType().getAnnotation(TableName.class).value();
                    } else {
                        foreignKeyModel.refTableName = column.getType().getSimpleName();
                    }

                    foreignKeyModel.isInline = false;
                    table.addConstraint(foreignKeyModel);
                }

                if (unique != null) {
                    UniqueModel uniqueModel = new UniqueModel();
                    uniqueModel.sourceColumnName = columnAnnotationModel.columnName;
                    currentColumnModel.hasInlineConstraint = true;
                    uniqueModel.isInline = true;
                    currentColumnModel.constraintModels.add(uniqueModel);
                }

                if (_default != null) {
                    DefaultModel defaultModel = new DefaultModel();
                    defaultModel.sourceColumnName = columnAnnotationModel.columnName;
                    defaultModel.defaultValue = ((Default) _default).value();
                    defaultModel.dataType = simpleNameOfDataType;
                    currentColumnModel.hasInlineConstraint = true;
                    defaultModel.isInline = true;
                    currentColumnModel.constraintModels.add(defaultModel);
                }

                if (notNull != null) {
                    NotNullModel notNullModel = new NotNullModel();
                    notNullModel.sourceColumnName = columnAnnotationModel.columnName;
                    currentColumnModel.hasInlineConstraint = true;
                    notNullModel.isInline = true;
                    currentColumnModel.constraintModels.add(notNullModel);
                }
                table.columnModels.add(currentColumnModel);
            }
            databaseTables.add(table);
        }
        prepareCreateTablesQueryStr();
    }

    static <T extends Tableable> long insert(T tableModel) {
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

            if (!field.isAnnotationPresent(Column.class)) continue;

            boolean isForeignKey = field.isAnnotationPresent(ForeignKey.class);

            String simpleNameOfDataType = (isForeignKey)
                    ? "Integer" : field.getType().getSimpleName();

            try {
                //Todo: Look up for default functionality in integers. It does not work
                boolean isDefaultNeeded = field.getAnnotation(Default.class) != null && field.get(tableModel) == null;

                //Todo: Make a primary key type Integer and see if the code below works properly. (I expect cannot cast exception)
                boolean isPrimaryNeeded = field.getAnnotation(PrimaryKey.class) != null && ((int) field.get(tableModel)) == 0;

                switch (simpleNameOfDataType) {
                    case "int":
                        if (isPrimaryNeeded) break;
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (int) field.get(tableModel));
                        break;
                    case "Integer":
                        if (isPrimaryNeeded) break;
                        if (isDefaultNeeded) break;
                        if (isForeignKey) {
                            Field prmrField = Utils.getPrimaryKeyField(field.getType());
                            if (prmrField == null)
                                throw new SqLiteManagerException("Referenced table " + field.getType().getSimpleName() + " does not have primary key");

                            contentValues.put(Utils.getMemberColumnName(field), (Integer) prmrField.get(field.get(tableModel)));
                            break;
                        }
                        contentValues.put(Utils.getMemberColumnName(field), (Integer) field.get(tableModel));
                        break;
                    case "String":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (String) field.get(tableModel));
                        break;
                    case "double":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (double) field.get(tableModel));
                        break;
                    case "Double":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (Double) field.get(tableModel));
                        break;
                    case "float":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (float) field.get(tableModel));
                        break;
                    case "Float":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (Float) field.get(tableModel));
                        break;
                    case "short":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (short) field.get(tableModel));
                        break;
                    case "Short":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (Short) field.get(tableModel));
                        break;
                    case "long":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (long) field.get(tableModel));
                        break;
                    case "Long":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (Long) field.get(tableModel));
                        break;
                    case "char":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (String) field.get(tableModel));
                        break;
                    case "byte":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (byte) field.get(tableModel));
                        break;
                    case "Byte":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (Byte) field.get(tableModel));
                        break;
                    case "boolean":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (boolean) field.get(tableModel));
                        break;
                    case "Boolean":
                        if (isDefaultNeeded) break;
                        contentValues.put(Utils.getMemberColumnName(field), (Boolean) field.get(tableModel));
                        break;
                    default:
                        throw new UnknownDatatypeException(TAG + ": " + field.getType().getSimpleName() + " and " + simpleNameOfDataType + " is not supported. Unknown type from Cursor!");
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

    //TODO: Make it compatible with several foreign key!
    private String getStringForeignKey(String columnWithForeignKey, String mainTableName) {
        String refColName = "";
        String refTableName = "";
        String result;

        for (TableModel table : databaseTables) {
            for (ConstraintModel constraintModel : table.getConstraintModels()) {
                if (constraintModel.sourceColumnName.equals(columnWithForeignKey)) {
                    if (constraintModel instanceof ForeignKeyModel) {
                        refColName = ((ForeignKeyModel) constraintModel).refColumnName;
                        refTableName = ((ForeignKeyModel) constraintModel).refTableName;
                    }
                }
            }
        }
        result = " INNER JOIN " + refTableName + " ON " + mainTableName + "." + columnWithForeignKey + "=" + refTableName + "." + refColName + " ";
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Tableable> ArrayList<T> all(String tableName) {
        return sqLiteManager.selectMany(tableName, Utils.getTableClass(tableName, sqLiteManager.tableTypesList), null, null, null, null, null, null);
    }

    public static <T extends Tableable> ArrayList<T> all(Class<T> modelClass) {
        return sqLiteManager.selectMany(Utils.getTableName(modelClass), modelClass, null, null, null, null, null, null);
    }

    private Cursor selectManyCursor(String name, String[] args,
                                    String condition, SortOrder sortOrder,
                                    Integer limit, String columnWithForeignKey, String[] columns, String sortColumn) {

        SQLiteDatabase sqLiteDatabase = sqLiteManager.getReadableDatabase();
        String foreignKey = (columnWithForeignKey != null && !columnWithForeignKey.isEmpty()) ? getStringForeignKey(columnWithForeignKey, name) : "";
        String[] projectionn;

        if (columns == null) projectionn = null;
        else projectionn = getColumnsForSelect(sqLiteDatabase, name, columns);

        String sortText = null;

        if (sortColumn != null)
            sortText = sortColumn + " " + sortOrder.getKeyWord();
        String strLimit = (limit == null) ? null : limit.toString();

        Cursor cursor;

        cursor = sqLiteDatabase.query(
                name + foreignKey,
                projectionn,
                condition,
                args,
                null,
                null,
                sortText,
                strLimit
        );
        return cursor;
    }

    private Cursor selectManyModelCursor(String name, String[] args,
                                         String condition, SortOrder sortOrder,
                                         Integer limit, String[] columns, String sortColumn) {

        SQLiteDatabase sqLiteDatabase = sqLiteManager.getReadableDatabase();
        String[] projectionn;

        if (columns == null) projectionn = null;
        else projectionn = getColumnsForSelect(sqLiteDatabase, name, columns);

        String sortText = null;

        if (sortColumn != null)
            sortText = sortColumn + " " + sortOrder.getKeyWord();
        String strLimit = (limit == null) ? null : limit.toString();

        Cursor cursor;

        cursor = sqLiteDatabase.query(
                name,
                projectionn,
                condition,
                args,
                null,
                null,
                sortText,
                strLimit
        );
        return cursor;
    }

    private <T extends Tableable> ArrayList<T> selectMany(String name, Class<T> modelClass, String[] args,
                                                          String condition, SortOrder sortOrder,
                                                          Integer limit,
                                                          String[] columns, String sortColumn) {

        Cursor cursor = selectManyModelCursor(name, args, condition,
                sortOrder, limit, columns, sortColumn);

        ArrayList<T> tableModels = new ArrayList<>();

        Field[] fields = modelClass.getFields();

        Arrays.sort(fields, new Comparator<Field>() {
            @Override
            public int compare(Field field, Field t1) {
                return field.getName().compareTo(t1.getName());
            }
        });

        if (cursor.moveToFirst()) {
            do {
                final T tableModel;
                try {
                    tableModel = modelClass.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Not successfully instantiated:" + " " + e.getMessage());
                }
                int index = 0;

                for (final Field field : fields) {
                    String simpleNameOfDataType = field.getType().getSimpleName();

                    SqlResponse result = Utils.readingSwitchAction(simpleNameOfDataType, field, tableModel, index, cursor, new AbstractDefaultCase() {
                        @Override
                        public void onDefault(Field field, int indexx, Cursor cursorr) {
                            try {
                                field.set(tableModel, find((Tableable) field.getType().newInstance(), cursorr.getInt(indexx)));
                            } catch (Exception e) {
                                throw new SqLiteManagerException(e.getMessage());
                            }
                        }
                    });
                    if (result == SqlResponse.Failed) continue;
                    index++;
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
        return null;
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
        private String sortColumn;
        private String columnWithForeignKey;

        public Select(String tableName) {
            this.tableName = tableName;
            tableClass = Utils.getTableClass(tableName, sqLiteManager.tableTypesList);
        }

        public <T extends Tableable> Select(Class<T> tableClass) {
            this.tableClass = tableClass;
            this.tableName = Utils.getTableName(tableClass);
        }

        @SuppressWarnings("unchecked")
        public <T extends Tableable> List<T> getList() {
            return sqLiteManager.selectMany(tableName, tableClass, args,
                    condition, sortOrder, limit, columns, sortColumn);
        }

        public Cursor getCursor() {
            return sqLiteManager.selectManyCursor(tableName, args,
                    condition, sortOrder, limit, columnWithForeignKey, columns, sortColumn);
        }

        public Select where(String condition, String... args) {
            this.condition = condition;
            List<String> list = new ArrayList<>(Arrays.asList(args));
            this.args = list.toArray(new String[list.size()]);
            return this;
        }

        public Select sort(String sortColumn, SortOrder order) {
            this.sortOrder = order;
            this.sortColumn = sortColumn;
            if (SortOrder.RANDOM == order) {
                this.sortColumn = "";
            }

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

        //Todo: Add 'first()' here. Which will return one Model


        //TODO: Make it compatible with several foreign key!
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


    static <T extends Tableable> int update(T tableModel) {
        SQLiteDatabase writable = sqLiteManager.getWritableDatabase();
        String whereClause = null;
        String[] whereArgs = null;

        Field[] fields = tableModel.getClass().getFields();
        ContentValues contentValues = new ContentValues();

        for (Field field : fields) {
            String colName;
            if (Utils.isColumn(field)) colName = Utils.getMemberColumnName(field);
            else continue;

            try {
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    whereClause = Utils.getMemberColumnName(field) + "=?";
                    int idValue = (int) field.get(tableModel);
                    if (idValue == 0) return -1;
                    whereArgs = new String[]{String.format(Locale.getDefault(), "%d", idValue)};
                }

                contentValues.put(colName, field.get(tableModel).toString());
            } catch (Exception e) {
                throw new SqLiteManagerException(e.getMessage());
            }
        }
        if (whereClause == null) {
            Log.e(TAG, "No primary key value found in object!");
            return -1;
        }
        return writable.updateWithOnConflict(Utils.getTableName(tableModel.getClass()), contentValues, whereClause, whereArgs, SQLiteDatabase.CONFLICT_IGNORE);
    }


    public static <T extends Tableable> T find(Class<T> clazz, Integer id) {
        try {
            return find(clazz.newInstance(), id);
        } catch (Exception e) {
            throw new SqLiteManagerException("Failed to instantiate: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Tableable> T find(String tableName, Integer id) {
        try {
            return find((T) Utils.getTableClass(tableName, sqLiteManager.tableTypesList).newInstance(), id);
        } catch (Exception e) {
            throw new SqLiteManagerException("Failed to instantiate: " + e.getMessage());
        }
    }

    static <T extends Tableable> T find(final T tableModel, Integer id) {
        Cursor cursor;
        SQLiteDatabase sqLiteDatabase = sqLiteManager.getReadableDatabase();
        String name = Utils.getTableName(tableModel.getClass());
        String memberColumnName = Utils.getMemberColumnName(Utils.getPrimaryKeyField(tableModel.getClass()));

        cursor = sqLiteDatabase.query(
                name,
                null,
                memberColumnName + "=?",
                new String[]{id.toString()},
                null,
                null,
                null
        );

        Field[] fields = tableModel.getClass().getFields();

        Arrays.sort(fields, new Comparator<Field>() {
            @Override
            public int compare(Field field, Field t1) {
                return field.getName().compareTo(t1.getName());
            }
        });

        if (cursor.moveToFirst()) {
            int index = 0;
            for (final Field field : fields) {
                String simpleNameOfDataType = field.getType().getSimpleName();

                SqlResponse response = Utils.readingSwitchAction(simpleNameOfDataType, field, tableModel, index, cursor, new AbstractDefaultCase() {
                    @Override
                    public void onDefault(Field field, int indexx, Cursor cursorr) {

                        try {
                            field.set(tableModel, find((Tableable) field.getType().newInstance(), cursorr.getInt(indexx)));
                        } catch (Exception e) {
                            throw new SqLiteManagerException(e.getMessage());
                        }
                    }
                });
                if (response == SqlResponse.Failed) continue;
                index++;
            }
            cursor.close();
            return tableModel;
        }
        cursor.close();
        return tableModel;
    }
}
