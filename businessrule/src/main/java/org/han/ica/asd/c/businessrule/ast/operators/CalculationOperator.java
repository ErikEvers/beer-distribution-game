package org.han.ica.asd.c.businessrule.ast.operators;

import org.han.ica.asd.c.businessrule.ast.ASTNode;

import java.util.Objects;

public class CalculationOperator extends ASTNode {
    private String operator;

    public CalculationOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "CalO(" + operator + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        CalculationOperator calculationOperator = (CalculationOperator) o;
        return Objects.equals(operator, calculationOperator.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator);
    }
}
