package org.han.ica.asd.c.businessrule.ast.operators;

import org.han.ica.asd.c.businessrule.ast.ASTNode;

import java.util.Objects;

public class ComparisonOperator extends ASTNode {
    private String operator;

    public ComparisonOperator(String operator) {
        this.operator = findComparisonOperator(operator);
    }

    private String findComparisonOperator(String operator) {
        if ("is".equals(operator) || "=".equals(operator) || "equal".equals(operator)) {
            return "==";
        } else if ("!=".equals(operator) || operator.contains("not equal")) {
            return "!=";
        } else if (operator.contains("greater") || operator.contains("higher")) {
            return ">";
        } else {
            return "<";
        }
    }

    @Override
    public String toString() {
        return "ComO(" + operator + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        ComparisonOperator comparisonOperator = (ComparisonOperator) o;
        return Objects.equals(operator, comparisonOperator.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator);
    }
}
