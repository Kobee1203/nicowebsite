package org.nds.dbdroid.query;

import java.util.LinkedList;
import java.util.List;

import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.query.Condition.Operation;
import org.nds.dbdroid.query.Conjunction.LogicalConjunction;
import org.nds.dbdroid.type.DbDroidType;
import org.nds.dbdroid.type.TypedValue;

public class Query {

    private final DataBaseManager dataBaseManager;

    // private final Map<String, TypedValue> parameters = new HashMap<String, TypedValue>();
    private final LinkedList<Expression> expressions = new LinkedList<Expression>();

    private final Class<?> entityClass;

    private boolean distinct;
    private String groupBy;
    private String having;
    private String orderBy;
    private int firstRow = -1;
    private int maxRows = -1;

    public Query(DataBaseManager dataBaseManager, Class<?> entityClass) {
        this.dataBaseManager = dataBaseManager;
        this.entityClass = entityClass;
    }

    public List<?> queryList() {
        return dataBaseManager.queryList(this);
    }

    public Query add(Expression expression) {
        expressions.add(expression);
        return this;
    }

    public static Condition createCondition(String name, Object val, DbDroidType type, Operation operation) {
        Condition condition = new Condition(name, new TypedValue(val, type), operation);
        return condition;
    }

    public static Conjunction createConjunction(Expression expression1, Expression expression2, LogicalConjunction logicalConjunction) {
        Conjunction conjunction = new Conjunction(expression1, expression2, logicalConjunction);
        return conjunction;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public Query setDistinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public Query groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public Query having(String having) {
        this.having = having;
        return this;
    }

    public String getHaving() {
        return having;
    }

    public Query orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public Query setFirstRow(int firstRow) {
        this.firstRow = firstRow;
        return this;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public Query setMaxRows(int maxRows) {
        this.maxRows = maxRows;
        return this;
    }

    public int getMaxRows() {
        return maxRows;
    }
}
