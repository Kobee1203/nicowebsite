package org.nds.dbdroid.sqlite;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.azeckoski.reflectutils.ReflectUtils;
import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.exception.DBDroidException;
import org.nds.dbdroid.helper.EntityHelper;
import org.nds.dbdroid.helper.Field;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLiteDataBaseManager extends DataBaseManager {

	private static final Logger log = Logger.getLogger(SQLiteDataBaseManager.class);
	
	private SQLiteHelper sqliteHelper;
	
	public SQLiteDataBaseManager(InputStream config, Context context, String name, CursorFactory factory, int version) throws DBDroidException {
		super(config);
		sqliteHelper = new SQLiteHelper(context, name, factory, version);
	}
	
	class SQLiteHelper extends SQLiteOpenHelper {
		//The Android's default system path of your application database.
	    private static final String DB_PATH_TOKEN = "@PACKAGE@"; 
		private static final String DB_PATH = "/data/data/"+DB_PATH_TOKEN+"/databases/";
	    private final String dbPath;
	    
	    private final Context context;
		private final String name;
		
		public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
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
			if(getDatabase() != null) {
				getDatabase().close();
			}
			super.close();
			log.debug("database closed.");
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
	    	try {
	        	//Open your local db as the input stream
	        	InputStream myInput = this.context.getAssets().open(name);
	     
	        	// Path to the just created empty db
	        	String outFileName = dbPath + name;
	     
	        	//Open the empty db as the output stream
	        	OutputStream myOutput = new FileOutputStream(outFileName);
	     
	        	//transfer bytes from the inputfile to the outputfile
	        	byte[] buffer = new byte[1024];
	        	int length;
	        	while ((length = myInput.read(buffer))>0){
	        		myOutput.write(buffer, 0, length);
	        	}
	     
	        	//Close the streams
	        	myOutput.flush();
	        	myOutput.close();
	        	myInput.close();
			} catch (IOException e) {
	    		throw new Error("Error copying database", e);

	    	}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//this.context.deleteDatabase(name);
		}	
	}

	@Override
	public void open() {
		sqliteHelper.open();
	}

	@Override
	public void close() {
		sqliteHelper.close();
	}

	@Override
	protected void onCreateTable(String tableName, List<Field> fields) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onUpdateTable(String tableName, List<Field> fields) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResetTable(String tableName, List<Field> fields) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Object entity) {
		String tableName = EntityHelper.getTableName(entity.getClass());
		Field idField = EntityHelper.getIdField(entity);	
		String columnName = EntityHelper.getColumnName(idField.getFieldName(), entity.getClass());
		
		sqliteHelper.getDatabase().delete(tableName, columnName + " = ?", new String[]{(String)idField.getFieldValue()});
	}

	@Override
	public <T> List<T> findAll(Class<T> entityClazz) {
		List<T> entities = null;

		String tableName = EntityHelper.getTableName(entityClazz);
		Cursor cursor = sqliteHelper.getDatabase().query(tableName, null, null, null, null, null, null);
		if(cursor.moveToFirst()) {
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
		// TODO Auto-generated method stub

	}

	private <T> T makeEntity(Class<T> entityClazz, Cursor cursor) {
		T entity = ReflectUtils.getInstance().constructClass(entityClazz);
		for(String columnName : cursor.getColumnNames()) {
			int idx = cursor.getColumnIndex(columnName);
			Field field = EntityHelper.getFieldByColumnName(columnName, entity);
			if(field == null) {
				throw new IllegalArgumentException("No fields are found for the column name is " + columnName);
			}
			
			Object value = getValue(idx, field.getFieldType(), cursor);
			
			ReflectUtils.getInstance().setFieldValue(entity, field.getFieldName(), value);
		}
		
		return entity;
	}
	
	private Object getValue(int idx, Class<?> fieldType, Cursor cursor) {
		Object value  = null;
		if(!cursor.isNull(idx)) {
			if (Double.class.equals(fieldType) || Float.class.equals(fieldType)) {
				value = cursor.getDouble(idx);
	        } else if (Number.class.equals(fieldType) || Long.class.equals(fieldType)) {
	        	value = cursor.getLong(idx);
	        } else if (Boolean.class.equals(fieldType)) {
	        	long v = cursor.getLong(idx);
	            if (v == 1) {
	            	value = true;
	            } else {
	            	value = false;
	            }
	        } else if (Integer.class.equals(fieldType)){
	        	value = cursor.getInt(idx);
	        } else if (Short.class.equals(fieldType)){
	        	value = cursor.getShort(idx);
	        } else if (byte[].class.equals(fieldType)){
	        	value = cursor.getBlob(idx);
	        } else {
	        	value = cursor.getString(idx);
	        }
		}
		
		return value;
	}
}
