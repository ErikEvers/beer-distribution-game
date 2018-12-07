package org.han.ica.asd.c.businessrule.parser.ast.operators;

import java.util.Objects;

public class CalculationOperator extends Operator {
    private String prefix = "CalO(";
    private String suffix = ")";

    public CalculationOperator(String operator) {
        this.operatorVal = operator;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder.append(prefix).append(operatorVal).append(suffix);
    }
}
