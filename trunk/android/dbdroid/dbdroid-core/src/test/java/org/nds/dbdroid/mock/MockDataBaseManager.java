package org.nds.dbdroid.mock;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.azeckoski.reflectutils.ReflectUtils;
import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.annotation.Id;
import org.nds.dbdroid.exception.DBDroidException;
import org.nds.dbdroid.helper.Field;
import org.nds.dbdroid.log.Logger;

public class MockDataBaseManager extends DataBaseManager {

    private static final Logger log = Logger.getLogger(MockDataBaseManager.class);

    private static final Map<Class<?>, Map<String, Object>> entities = new HashMap<Class<?>, Map<String, Object>>();

    public MockDataBaseManager(InputStream config) throws DBDroidException {
        super(config);
    }

    @Override
    public void open() {
        log.debug("## open");
    }

    @Override
    public void close() {
        log.debug("## close");
    }

    @Override
    protected void onCreateTable(String tableName, List<Field> fields) {
        log.debug("## create table " + tableName + " " + fields);
    }

    @Override
    protected void onUpdateTable(String tableName, List<Field> fields) {
        log.debug("## update table " + tableName + " " + fields);
    }

    @Override
    protected void onResetTable(String tableName, List<Field> fields) {
        log.debug("## reset table " + tableName + " " + fields);
    }

    @Override
    public void delete(Object entity) {
        log.debug("## delete " + entity);
    }

    @Override
    public <E> List<E> findAll(Class<E> entityClass) {
        log.debug("## find all (" + entityClass + ")");

        List<E> list = null;

        Map<String, Object> objects = entities.get(entityClass);
        if (objects != null) {
            list = new ArrayList<E>();
            for (Object o : objects.values()) {
                list.add((E) o);
            }
        }

        return list;
    }

    @Override
    public <E> E findById(String id, Class<E> entityClazz) {
        log.debug("## find by id " + id);

        E object = null;

        Map<String, Object> objects = entities.get(entityClazz);
        if (objects != null) {
            object = (E) objects.get(id);
        }

        return object;
    }

    @Override
    public <E> E saveOrUpdate(E entity) {
        log.debug("## save or update (" + entity + ")");

        Map<String, Object> objects = entities.get(entity.getClass());
        if (objects == null) {
            objects = new HashMap<String, Object>();
        }
        String id = ReflectUtils.getInstance().getFieldValueAsString(entity, null, Id.class);
        objects.put(id, entity);

        entities.put(entity.getClass(), objects);

        return entity;
    }

    @Override
    public void rawQuery(String query) {
        log.debug("## run raw query: " + query);
        if (query.toUpperCase().startsWith("INSERT INTO")) {
            int beginIndex = query.toUpperCase().indexOf("INSERT INTO") + "INSERT INTO".length();
            int endIndex = query.toUpperCase().indexOf("VALUES");
            String tableNameWithFields = query.substring(beginIndex, endIndex);
            // Retrieve table name
            String tableName = tableNameWithFields.substring(0, tableNameWithFields.indexOf("("));

            beginIndex = tableNameWithFields.toUpperCase().indexOf("(") + "(".length();
            endIndex = tableNameWithFields.toUpperCase().indexOf(")");
            String concatFields = tableNameWithFields.substring(beginIndex, endIndex);
            // Retrieve fields
            String[] fields = concatFields.split(",");

            String subQuery = query.substring(query.toUpperCase().indexOf("VALUES") + "VALUES".length());
            beginIndex = subQuery.toUpperCase().indexOf("(") + "(".length();
            endIndex = subQuery.toUpperCase().indexOf(")");
            String concatValues = subQuery.substring(beginIndex, endIndex);
            // Retrieve fields values
            String[] values = concatValues.split(",");

            Class<?> entityClass = getEntityFromTableName(tableName.trim().toUpperCase());
            if (entityClass != null) {
                Object entity = ReflectUtils.getInstance().constructClass(entityClass);
                for (int i = 0; i < fields.length; i++) {
                    String fieldName = fields[i].replace("'", "").trim();
                    Object value = values[i].replace("'", "").trim();
                    Object fieldType = ReflectUtils.getInstance().getFieldType(entityClass, fieldName);
                    if(fieldType.equals(byte[].class)) {
                        value = value.toString().getBytes();
                    }
                    ReflectUtils.getInstance().setFieldValue(entity, fieldName, value, true);
                }
                saveOrUpdate(entity);
            } else {
                log.warn("Entity not found from table name: " + tableName);
            }
        }
    }
}
