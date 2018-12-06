package org.han.ica.asd.c.businessrule.ast.operators;

import org.han.ica.asd.c.businessrule.ast.ASTNode;

import java.util.Objects;

public class BooleanOperator extends ASTNode {
    private String operator;

    public BooleanOperator(String operator) {
        this.operator = findBooleanOperator(operator);
    }

    private String findBooleanOperator(String operator) {
        if ("and".equals(operator)) {
            return "&&";
        } else {
            return "||";
        }
    }

    @Override
    public String toString() {
        return "BoolO(" + operator + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        BooleanOperator booleanOperator = (BooleanOperator) o;
        return Objects.equals(operator, booleanOperator.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator);
    }
}
