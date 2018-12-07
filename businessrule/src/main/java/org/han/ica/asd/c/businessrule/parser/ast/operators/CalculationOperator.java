package org.han.ica.asd.c.businessrule.parser.ast.operators;

import org.han.ica.asd.c.businessrule.parser.ast.ASTNode;

import java.util.Objects;

public class CalculationOperator extends ASTNode {
    private String prefix = "CalO(";
    private String suffix = ")";
    private String operator;

    public CalculationOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder.append(prefix).append(operator).append(suffix);
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
