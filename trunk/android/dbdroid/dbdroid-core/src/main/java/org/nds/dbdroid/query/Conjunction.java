package org.nds.dbdroid.query;

import org.nds.dbdroid.DataBaseManager;

public class Conjunction implements Expression {
    public enum LogicalConjunction {
        AND,
        OR
    }

    private final Expression expression1;
    private final Expression expression2;
    private final LogicalConjunction logicalConjunction;

    public Conjunction(Expression expression1, Expression expression2, LogicalConjunction logicalConjunction) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.logicalConjunction = logicalConjunction;
    }

    public String toQueryString(DataBaseManager dbManager) {
        return dbManager.toExpressionString(this);
    }

    public Expression getExpression1() {
        return expression1;
    }

    public Expression getExpression2() {
        return expression2;
    }

    public LogicalConjunction getLogicalConjunction() {
        return logicalConjunction;
    }
}
