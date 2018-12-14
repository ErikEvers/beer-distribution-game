package org.han.ica.asd.c.businessrule.parser.ast.operators;

public class CalculationOperator extends Operator {
    private final String prefix = "CalO(";
    private final String suffix = ")";

    public CalculationOperator() {
    }

    /**
     * Constructor
     *
     * @param operator The calculation operator
     */
    public CalculationOperator(String operator) {
        this.operatorVal = operator;
    }

    /**
     * Sets the operatorValue of the {@link Operator}
     *
     * @param value The operator value to add
     * @return Returns the instance of the {@link CalculationOperator}
     */
    @Override
    public CalculationOperator addValue(String value) {
        operatorVal = value;
        return this;
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
     * Calls the hashCode function of its super class
     *
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     *
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder.append(prefix).append(operatorVal).append(suffix);
    }

}
