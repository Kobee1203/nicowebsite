package org.nds.dbdroid.helper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.azeckoski.reflectutils.ClassFields;
import org.azeckoski.reflectutils.ReflectUtils;
import org.nds.dbdroid.annotation.Column;
import org.nds.dbdroid.annotation.Entity;
import org.nds.dbdroid.annotation.Id;

public final class EntityHelper {

    private static final Log log = LogFactory.getLog(EntityHelper.class);
	
	private EntityHelper() {
	}
	
	public static String getTableName(Class<?> entityClass) {
		// Analyze class
		ClassFields<?> cf = ReflectUtils.getInstance().analyzeClass(entityClass);
		Entity entity = cf.getClassAnnotation(Entity.class);
		String tableName = entity.name();
		if(tableName == null) {
			throw new IllegalArgumentException("Table name is NULL for Entity " + entityClass.getClass().getCanonicalName());
		} else if("".equals(tableName)) {
			tableName = entityClass.getName().toLowerCase();
			log.debug("Table name is empty -> Define table with lowercase Entity class name: " + tableName);
		}
		
		return tableName;
	}
	
	public static List<Field> getFields(Class<?> entityClass) {
		List<Field> fields = new ArrayList<Field>();
		// Analyze class
		ClassFields<?> cf = ReflectUtils.getInstance().analyzeClass(entityClass);
		for(String fieldName : cf.getFieldNames()) {
			Class<?> fieldType = cf.getFieldType(fieldName);
			if(fieldType != null) {
				Field field = new Field(fieldName, null, cf.getFieldAnnotations(fieldName), fieldType);
				fields.add(field);
			}
		}
		
		return fields;
	}
	
	public static Field getIdField(Object entity) {
		// Analyze class
		ClassFields<?> cf = ReflectUtils.getInstance().analyzeClass(entity.getClass());
		
		String idValue = null;
		
		// Retrieve the ID field
		String fieldName = cf.getFieldNameByAnnotation(Id.class);
		if(fieldName == null ) {
			idValue = ReflectUtils.getInstance().getFieldValueAsString(entity, "_id", null);
			if(idValue == null) {
				throw new IllegalArgumentException("No fields are found with the 'Id' annotation or '_id' name.");
			}
			// Set the fieldName;
			fieldName = "_id";
		} else {
			idValue = ReflectUtils.getInstance().getFieldValueAsString(entity, fieldName, null);
		}
		
		Field field = new Field(fieldName, idValue, cf.getFieldAnnotations(fieldName), cf.getFieldType(fieldName));
		
		return field;
	}
	
	public static Field getField(String fieldName, Object entity) {
		Field field = null;
		
		// Analyze class
		ClassFields<?> cf = ReflectUtils.getInstance().analyzeClass(entity.getClass());
		
		Class<?> fieldType = cf.getFieldType(fieldName);
		if(fieldType != null) {
			field = new Field(fieldName, ReflectUtils.getInstance().getFieldValue(entity, fieldName), cf.getFieldAnnotations(fieldName), fieldType);
		}
		
		return field;
	}

	public static Field getFieldByColumnName(String columnName, Object entity) {
		// Analyze class
		ClassFields<?> cf = ReflectUtils.getInstance().analyzeClass(entity.getClass());
		
		// Search a field with a Column annotation where name is columnName
		List<String> fieldNames = cf.getFieldNamesWithAnnotation(Column.class);
		for(String fieldName : fieldNames) {
			Set<Annotation> annotations = cf.getFieldAnnotations(fieldName);
			for(Annotation annotation : annotations) {
				// Retrieve the column name is defined 
				if(Column.class.equals(annotation.annotationType())) {
					String colName = ((Column)annotation).name();
					if(columnName.equals(colName)){
						Field field = new Field(fieldName, ReflectUtils.getInstance().getFieldValue(entity, fieldName), cf.getFieldAnnotations(fieldName), cf.getFieldType(fieldName));
						return field;
					}
				}
			}
		}
		
		// Search a field where field name is columnName
		fieldNames = cf.getFieldNames();
		for(String fieldName : fieldNames) {
			if(columnName.equals(fieldName)) {
				Field field = new Field(fieldName, ReflectUtils.getInstance().getFieldValue(entity, fieldName), cf.getFieldAnnotations(fieldName), cf.getFieldType(fieldName));
				return field;
			}
		}
		
		return null;
	}
	
	public static String getColumnName(String fieldName, Class<?> clazz) {
		String columnName = null;
		
		// Analyze class
		ClassFields<?> cf = ReflectUtils.getInstance().analyzeClass(clazz);
		
		Set<Annotation> annotations = cf.getFieldAnnotations(fieldName);
		for(Annotation annotation : annotations) {
			// Retrieve the column name is defined 
			if(Column.class.equals(annotation.annotationType())) {
				columnName = ((Column)annotation).name();
			}
		}
		if(columnName == null) {
			// Set the column name if Column annotation not defined
			columnName = fieldName.toLowerCase();
		}
		
		return columnName;
	}
	
}
