package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

public class DivideOperation extends Operation {
    private String prefix = "Div(";
    private String suffix = ")";

    public DivideOperation() {
        super.calculationOperator = new CalculationOperator("/");
    }

    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, prefix, suffix);
    }
}
