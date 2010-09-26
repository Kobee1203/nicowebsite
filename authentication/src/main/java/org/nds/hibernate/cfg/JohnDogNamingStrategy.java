package org.nds.hibernate.cfg;

import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.cfg.NamingStrategy;

public class JohnDogNamingStrategy extends ImprovedNamingStrategy implements NamingStrategy {

	private static final long serialVersionUID = -4347715643412982807L;
	
	@Override
	public String classToTableName(String className) {
	    return super.classToTableName(className);
	}
	
	@Override
	public String propertyToColumnName(String propertyName) {
	    return super.propertyToColumnName(propertyName);
	}
	
	@Override
	public String columnName(String columnName) {
	    return super.columnName(columnName);
	}
	
	@Override
	public String tableName(String tableName) {
	    return super.tableName(tableName);
	}
	
}
