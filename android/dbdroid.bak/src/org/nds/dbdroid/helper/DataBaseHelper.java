package org.nds.dbdroid.helper;

import java.util.List;

public interface DataBaseHelper {

	static final String TAG = DataBaseHelper.class.getName();
	
	void open();
	
	void close();

	void delete(Object entity);

	<T> List<T> findAll(Class<T> entityClass);
	
	<T> T findById(String id, Class<T> entityClazz);
	
	<T> T saveOrUpdate(T entity);
}
