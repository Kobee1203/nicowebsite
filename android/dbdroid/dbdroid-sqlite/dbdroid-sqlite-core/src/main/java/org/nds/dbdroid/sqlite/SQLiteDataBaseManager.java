package org.nds.dbdroid.sqlite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.reflect.ConstructorUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.exception.DBDroidException;
import org.nds.dbdroid.helper.EntityHelper;
import org.nds.dbdroid.log.Logger;
import org.nds.dbdroid.type.DataType;
import org.nds.dbdroid.type.DbDroidType;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

public class SQLiteDataBaseManager extends DataBaseManager {

    private static final Logger log = Logger.getLogger(SQLiteDataBaseManager.class);

    private enum DataTypeAffinity {
        INTEGER, TEXT, BLOB, REAL, NUMERIC
    }

    private static final Map<DbDroidType, String> MAPPED_DATA_TYPES;

    static {
        HashMap<DbDroidType, String> basics = new HashMap<DbDroidType, String>();
        basics.put(DbDroidType.BOOLEAN);
        basics.put(DbDroidType.LONG);
        basics.put(DbDroidType.SHORT);
        basics.put(DbDroidType.INTEGER);
        basics.put(DbDroidType.BYTE);
        basics.put(DbDroidType.FLOAT);
        basics.put(DbDroidType.DOUBLE);
        basics.put(DbDroidType.CHARACTER);
        basics.put(DbDroidType.STRING);
        basics.put(DbDroidType.TIMESTAMP);
        basics.put(DbDroidType.TIME);
        basics.put(DbDroidType.DATE);
        basics.put(DbDroidType.BIG_DECIMAL);
        basics.put(DbDroidType.BIG_INTEGER);
        basics.put(DbDroidType.LOCALE);
        basics.put(DbDroidType.CALENDAR);
        basics.put(DbDroidType.TIMEZONE);
        basics.put(DbDroidType.OBJECT);
        basics.put(DbDroidType.CLASS);
        basics.put(DbDroidType.BINARY);
        basics.put(DbDroidType.WRAPPER_BINARY);
        basics.put(DbDroidType.CHAR_ARRAY);
        basics.put(DbDroidType.CHARACTER_ARRAY);
        basics.put(DbDroidType.BLOB);
        basics.put(DbDroidType.CLOB);
        basics.put(DbDroidType.SERIALIZABLE);

        MAPPED_DATA_TYPES = Collections.unmodifiableMap(basics);
    }

    /*private enum DataType {
        INT
        INTEGER
        TINYINT
        SMALLINT                 // INTEGER 
        MEDIUMINT
        BIGINT
        UNSIGNED BIG INT
        INT2
        INT8 
        
        CHARACTER(20)
        VARCHAR(255)
        VARYING CHARACTER(255)   // TEXT 
        NCHAR(55)
        NATIVE CHARACTER(70)
        NVARCHAR(100)
        TEXT
        CLOB 
        
        BLOB                     // NONE
        
        REAL
        DOUBLE
        DOUBLE PRECISION         // REAL 
        FLOAT 
        
        NUMERIC
        DECIMAL(10,5)
        BOOLEAN                  // NUMERIC 
        DATE
        DATETIME 
    }*/

    private final SQLiteHelper sqliteHelper;

    public SQLiteDataBaseManager(InputStream config, Context context, String name, CursorFactory factory, int version) throws DBDroidException {
        super(config);
        sqliteHelper = new SQLiteHelper(context, name, factory, version);
    }

    class SQLiteHelper extends SQLiteOpenHelper {
        // The Android's default system path of your application database.
        private static final String DB_PATH_TOKEN = "@PACKAGE@";
        private static final String DB_PATH = "/data/data/" + DB_PATH_TOKEN + "/databases/";
        private final String dbPath;

        private final Context context;
        private final String name;

        public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name.substring(name.lastIndexOf("/") + 1), factory, version);
            this.context = context;
            this.dbPath = DB_PATH.replace(DB_PATH_TOKEN, this.context.getPackageName());
            log.debug("[instance] database path: " + this.dbPath);
            this.name = name;
        }

        public SQLiteDatabase getDatabase() {
            return this.getWritableDatabase();
        }

        public void open() {
            getDatabase();
            log.debug("database opened.");
        }

        @Override
        public synchronized void close() {
            if (getDatabase() != null) {
                getDatabase().close();
            }
            super.close();
            log.debug("database closed.");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                // Open your local db as the input stream
                InputStream myInput = this.context.getAssets().open(name);

                // Path to the just created empty db
                File outFileName = new File(dbPath + name);
                if (!outFileName.getParentFile().mkdirs()) {
                    throw new Error("Error creating folder '" + outFileName.getParentFile().getAbsolutePath() + "' to copy database");
                }

                // Open the empty db as the output stream
                OutputStream myOutput = new FileOutputStream(outFileName);

                // transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                // Close the streams
                myOutput.flush();
                myOutput.close();
                myInput.close();
            } catch (IOException e) {
                throw new Error("Error copying database", e);

            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // this.context.deleteDatabase(name);
        }
    }

    @Override
    public void onOpen() {
        sqliteHelper.open();
    }

    @Override
    public void onClose() {
        sqliteHelper.close();
    }

    @Override
    protected void onCreateTable(String tableName, Field[] fields) {
        String sql = "CREATE TABLE " + tableName;
        for (Field field : fields) {
            field.getType();
        }
        sqliteHelper.getDatabase().execSQL(sql);
    }

    @Override
    protected void onUpdateTable(String tableName, Field[] fields) {
        sqliteHelper.getDatabase().execSQL(sql);
    }

    @Override
    protected void onResetTable(String tableName, Field[] fields) {
        sqliteHelper.getDatabase().execSQL(sql);
    }

    @Override
    public void delete(Object entity) {
        String tableName = EntityHelper.getTableName(entity.getClass());
        Field idField = EntityHelper.getIdField(entity.getClass());
        String columnName = EntityHelper.getColumnName(idField, entity.getClass());
        Object idFieldValue = null;
        try {
            idFieldValue = FieldUtils.readField(idField, entity, true);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        sqliteHelper.getDatabase().delete(tableName, columnName + " = ?", new String[] { (String) idFieldValue });
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClazz) {
        List<T> entities = null;

        String tableName = EntityHelper.getTableName(entityClazz);
        Cursor cursor = sqliteHelper.getDatabase().query(tableName, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            entities = new ArrayList<T>();
            do {
                // Make entity with columns values
                T entity = makeEntity(entityClazz, cursor);
                // Add new entity
                entities.add(entity);
            } while (cursor.moveToNext());
        }

        return entities;
    }

    @Override
    public <E> E findById(String id, Class<E> entityClazz) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <E> E saveOrUpdate(E entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void rawQuery(String query) {
        sqliteHelper.getDatabase().rawQuery(sql, selectionArgs)
    }

    private <T> T makeEntity(Class<T> entityClazz, Cursor cursor) {
        T entity = null;
        try {
            entity = ConstructorUtils.invokeConstructor(entityClazz, (Object[]) null);

            for (String columnName : cursor.getColumnNames()) {
                int idx = cursor.getColumnIndex(columnName);
                Field field = EntityHelper.getFieldByColumnName(columnName, entity.getClass());
                if (field == null) {
                    throw new IllegalArgumentException("No fields are found for the column name is " + columnName);
                }

                Object value = getValue(idx, field.getType(), cursor);

                FieldUtils.writeField(field, entity, value, true);
            }
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage(), e);
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
        }

        return entity;
    }

    private Object getValue(int idx, Class<?> fieldType, Cursor cursor) {
        Object value = null;
        if (!cursor.isNull(idx)) {
            Class<?> type = ClassUtils.primitiveToWrapper(fieldType);
            if (Boolean.TYPE.equals(type)) {
                long v = cursor.getLong(idx);
                if (v == 1) {
                    value = true;
                } else {
                    value = false;
                }
            } else if (Double.TYPE.equals(type) || Float.TYPE.equals(type)) {
                value = cursor.getDouble(idx);
            } else if (Integer.TYPE.equals(type)) {
                value = cursor.getInt(idx);
            } else if (Short.TYPE.equals(type)) {
                value = cursor.getShort(idx);
            } else if (Number.class.equals(type) || Long.TYPE.equals(type)) {
                value = cursor.getLong(idx);
            } else if (Character.TYPE.equals(type)) {
                value = (char) cursor.getInt(idx);
            } else if (byte[].class.equals(type)) {
                value = cursor.getBlob(idx);
            } else {
                value = cursor.getString(idx);
            }
        }

        return value;
    }

    @Override
    public DataType getDataType() {
        return new DataType(MAPPED_DATA_TYPES);
    }
}
