package org.nds.dbdroid.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nds.dbdroid.DataBaseManager;

public class AndroidDAO<T> implements GenericDAO<T, String> {

    private static final Log log = LogFactory.getLog(AndroidDAO.class);
	
	protected DataBaseManager dbManager;
	
	private Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public AndroidDAO(DataBaseManager dbManager) {
		this.dbManager = dbManager;
		this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public final Class<T> getEntityClass() {
		return entityClass;
	}
	
	public void delete(T entity) {
	    log.debug("Delete Entity");
		this.dbManager.delete(entity);
	}

	public List<T> findAll() {
		log.debug("Find all Entities");
		return (List<T>) this.dbManager.findAll(entityClass);
	}

	public T findById(String id) {
		log.debug("Find Entity by Id: " + id);
		return (T) this.dbManager.findById(id, entityClass);
	}

	public T saveOrUpdate(T entity) {
		log.debug("Save or Update Entity");
		return (T) this.dbManager.saveOrUpdate(entity);
	}

}
