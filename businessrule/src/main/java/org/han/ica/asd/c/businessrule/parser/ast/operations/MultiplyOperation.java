package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

public class MultiplyOperation extends Operation {
    private String prefix = "Mul(";
    private String suffix = ")";

    public MultiplyOperation() {
        super.calculationOperator = new CalculationOperator(OperationType.MUL.getOperation());
    }

    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, prefix, suffix);
    }
}
