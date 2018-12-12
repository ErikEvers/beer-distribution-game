package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

public class MultiplyOperation extends Operation {
    private String prefix = "Mul(";
    private String suffix = ")";

    /**
     * Constructor
     */
    public MultiplyOperation() {
        super.calculationOperator = new CalculationOperator(OperationType.MUL.getOperation());
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

    @Override
    public Value executeOperation(int left, int right) {
        return new Value(left * right);
    }
}
