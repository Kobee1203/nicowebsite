package org.nds.dbdroid.mock;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.azeckoski.reflectutils.ReflectUtils;
import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.exception.DBDroidException;
import org.nds.dbdroid.helper.Field;

public class MockDataBaseManager extends DataBaseManager {

	private static final Log log = LogFactory.getLog(MockDataBaseManager.class);

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

		return null;
	}

	@Override
	public <E> E findById(String id, Class<E> entityClazz) {
		log.debug("## find by id " + id);

		return null;
	}

	@Override
	public <E> E saveOrUpdate(E entity) {
		log.debug("## save or update (" + entity + ")");

		return null;
	}

	@Override
	public void rawQuery(String query) {
		log.debug("## run raw query: " + query);
		if (query.toUpperCase().startsWith("INSERT INTO")) {
			int beginIndex = query.toUpperCase().indexOf("INSERT INTO") + "INSERT INTO".length();
			int endIndex = query.toUpperCase().indexOf("VALUES");
			String tableName = query.substring(beginIndex, endIndex);
			beginIndex = query.toUpperCase().indexOf("(") + "(".length();
			endIndex = query.toUpperCase().indexOf(")");
			String concatValues = query.substring(beginIndex, endIndex);
			String[] values = concatValues.split(",");
			ReflectUtils.getInstance().constructClass(getEntityFromTableName(tableName));
		}
	}
}
