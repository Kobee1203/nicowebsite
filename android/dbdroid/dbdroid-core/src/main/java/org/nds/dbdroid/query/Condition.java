package org.nds.dbdroid.query;

import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.type.TypedValue;

public class Condition implements Expression {
    public enum Operation {
        EQUAL,
        NOT_EQUAL,
        LIKE,
        GREATER_THAN,
        LESS_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN_OR_EQUAL,
        IN,
        NOT_IN,
        IS_NULL,
        IS_NOT_NULL
    }

    private final String name;
    private final TypedValue typedValue;
    private final Operation operation;

    public Condition(String name, TypedValue typedValue, Operation operation) {
        this.name = name;
        this.typedValue = typedValue;
        this.operation = operation;
    }

    public String toQueryString(DataBaseManager dbManager) {
        return dbManager.toExpressionString(this);
    }

    public String getName() {
        return name;
    }

    public String getValue(QueryValueResolver queryValueResolver) {
        return queryValueResolver.toString(typedValue);
    }

    public Operation getOperation() {
        return operation;
    }
}
