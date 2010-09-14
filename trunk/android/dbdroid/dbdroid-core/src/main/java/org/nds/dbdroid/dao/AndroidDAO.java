package org.nds.dbdroid.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.nds.dbdroid.DataBaseManager;

import android.util.Log;

public class AndroidDAO<T> implements GenericDAO<T, String> {

	private static final String TAG = AndroidDAO.class.getName();
	
	protected DataBaseManager dbManager;
	
	private Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public AndroidDAO(DataBaseManager dbManager) {
		this.dbManager = dbManager;
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public void delete(T entity) {
		Log.d(TAG, "Delete Entity");
		this.dbManager.delete(entity);
	}

	public List<T> findAll() {
		Log.d(TAG, "Find all Entities");
		return (List<T>) this.dbManager.findAll(entityClass);
	}

	public T findById(String id) {
		Log.d(TAG, "Find Entity by Id: " + id);
		return (T) this.dbManager.findById(id, entityClass);
	}

	public T saveOrUpdate(T entity) {
		Log.d(TAG, "Save or Update Entity");
		return (T) this.dbManager.saveOrUpdate(entity);
	}

}
