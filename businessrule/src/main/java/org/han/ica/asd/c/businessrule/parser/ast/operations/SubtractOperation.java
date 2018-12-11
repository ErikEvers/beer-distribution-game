package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

public class SubtractOperation extends Operation {
    private String prefix = "Sub(";
    private String suffix = ")";

    public SubtractOperation() {
        super.calculationOperator = new CalculationOperator(OperationType.SUB.getOperation());
    }

    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, prefix, suffix);
    }
}
