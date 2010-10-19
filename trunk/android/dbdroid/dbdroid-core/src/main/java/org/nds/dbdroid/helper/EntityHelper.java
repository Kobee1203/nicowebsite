package org.nds.dbdroid.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.commons.lang.reflect.FieldUtils;
import org.nds.dbdroid.annotation.Column;
import org.nds.dbdroid.annotation.Entity;
import org.nds.dbdroid.annotation.Id;
import org.nds.dbdroid.log.Logger;
import org.nds.dbdroid.reflect.utils.AnnotationUtils;

public final class EntityHelper {

    private static final Logger log = Logger.getLogger(EntityHelper.class);

    private EntityHelper() {
    }

    public static String getTableName(Class<?> entityClass) {
        Entity entity = AnnotationUtils.getAnnotation(entityClass, Entity.class);
        String tableName = entity.name();
        if (tableName == null) {
            throw new IllegalArgumentException("Table name is NULL for Entity " + entityClass.getClass().getCanonicalName());
        } else if ("".equals(tableName)) {
            tableName = entityClass.getSimpleName().toUpperCase();
            log.debug("Table name is empty -> Define table with lowercase Entity class name: " + tableName);
        }

        return tableName;
    }

    public static Field getIdField(Class<?> entityClass) {
        // Retrieve the ID field
        java.lang.reflect.Field field = AnnotationUtils.findField(entityClass, Id.class);
        if (field == null) {
            field = FieldUtils.getField(entityClass, "_id", true);
            if (field == null) {
                throw new IllegalArgumentException("No fields are found with the 'Id' annotation or '_id' name.");
            }
        }

        return field;
    }

    public static Field getFieldByColumnName(String columnName, Class<?> entityClass) {
        // Search a field with a Column annotation where name is columnName
        Field[] fields = AnnotationUtils.findFields(entityClass, Column.class);
        for (Field field : fields) {
            Annotation[] annotations = AnnotationUtils.getAnnotations(field);
            for (Annotation annotation : annotations) {
                // Retrieve the column name is defined 
                if (Column.class.equals(annotation.annotationType())) {
                    String colName = ((Column) annotation).name();
                    if (columnName.equals(colName)) {
                        return field;
                    }
                }
            }
        }

        // Search a field where field name is columnName
        return FieldUtils.getField(entityClass, columnName, true);
    }

    public static String getColumnName(Field field, Class<?> clazz) {
        String columnName = null;

        if (field != null) {
            Annotation[] annotations = AnnotationUtils.getAnnotations(field);
            for (Annotation annotation : annotations) {
                // Retrieve the column name is defined 
                if (Column.class.equals(annotation.annotationType())) {
                    columnName = ((Column) annotation).name();
                }
            }
            if (columnName == null) {
                // Set the column name if Column annotation not defined
                columnName = field.getName().toLowerCase();
            }
        }

        return columnName;
    }

}