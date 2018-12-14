package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

public class SubtractOperation extends Operation {
    private String prefix = "Sub(";
    private String suffix = ")";

    /**
     * Constructor
     */
    public SubtractOperation() {
        super.calculationOperator = new CalculationOperator(OperationType.SUB.getOperation());
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, prefix, suffix);
    }
}
