package org.nds.dbdroid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query {

    private final DataBaseManager dataBaseManager;
    private final String sql;

    private final Map<String, Class> entities = new HashMap<String, Class>();

    public Query(DataBaseManager dataBaseManager, String sql) {
        this.dataBaseManager = dataBaseManager;
        this.sql = sql;
    }

    public List<?> queryList() {
        return dataBaseManager.queryList(this);
    }

    public Query addEntity(String alias, Class entity) {
        entities.put(alias, entity);
        return this;
    }
}
