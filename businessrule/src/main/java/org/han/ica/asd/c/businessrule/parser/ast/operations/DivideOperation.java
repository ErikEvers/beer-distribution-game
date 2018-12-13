package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

import java.util.Objects;

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

    /**
     * Executes the operation and returns the result of the operation
     *
     * @param left  the left value of the operation
     * @param right the right value of the operation
     * @return Returns the result of the Operation as a {@link Value}
     */
    @Override
    public Value executeOperation(int left, int right) {
        return new Value(left / right);
    }

    /**
     * Calls the equals function of its super class
     *
     * @param o Object that needs to be checked if it's equal to this object
     * @return Returns true or false depending on if it's equal or not
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * Hash function used for unit testing
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(prefix);
    }
}
