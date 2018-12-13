package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

public class DivideOperation extends Operation {
    private String prefix = "Div(";
    private String suffix = ")";

    /**
     * Constructor
     */
    public DivideOperation() {
        super.calculationOperator = new CalculationOperator(OperationType.DIV.getOperation());
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, prefix, suffix);
    }
}
