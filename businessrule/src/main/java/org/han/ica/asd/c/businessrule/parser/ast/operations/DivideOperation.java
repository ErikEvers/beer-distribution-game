package org.han.ica.asd.c.businessrule.parser.ast.operations;

import org.han.ica.asd.c.businessrule.parser.ast.operators.CalculationOperator;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Objects;

public class DivideOperation extends Operation {
    private static final String PREFIX = "Div(";

    @Inject
    private Provider<CalculationOperator> calculationOperatorProvider;

    public DivideOperation() {
    }
    @Inject
    public DivideOperation(Provider<CalculationOperator> calculationOperatorProvider) {
        this.calculationOperatorProvider = calculationOperatorProvider;
        super.calculationOperator = calculationOperatorProvider.get().addValue(OperationType.DIV.getOperation());
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        super.encode(stringBuilder, PREFIX);
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
        if(right == 0){
            return new Value(left);
        }
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
        return Objects.hash(PREFIX);
    }
}
