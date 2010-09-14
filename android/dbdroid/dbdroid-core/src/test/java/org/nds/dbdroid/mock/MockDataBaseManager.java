package org.nds.dbdroid.mock;

import java.io.InputStream;
import java.util.List;

import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.exception.DBDroidException;

public class MockDataBaseManager extends DataBaseManager {

	public MockDataBaseManager(InputStream config) throws DBDroidException {
		super(config);
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Object entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public <E> List<E> findAll(Class<E> entityClass) {
		// TODO Auto-generated method stub
		return null;
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

}
