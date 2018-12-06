package org.han.ica.asd.c.businessrule.parser.ast.operations;


import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

public class AddOperation extends Operation {
    private String prefix = "Add(";
    private String suffix = ")";

    public AddOperation() {
        super.calculationOperator = new CalculationOperator("+");
    }

    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, prefix, suffix);
    }
}
