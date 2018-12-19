package org.han.ica.asd.c.businessrule.parser.ast.operators;

public class BooleanOperator extends Operator {
    private static final String prefix = "BoolO(";

    /**
     * Constructor
     */
    public BooleanOperator() {
    }

    /**
     * Constructor
     *
     * @param operator The boolean operator as a word
     */
    public BooleanOperator(String operator) {
        this.operatorVal = findBooleanOperator(operator);
    }

    /**
     * Sets the operatorValue of the {@link Operator}
     *
     * @param value the booleanOperator to be set
     * @return Returns the instance of {@link BooleanOperator}
     */
    @Override
    public BooleanOperator addValue(String value) {
        operatorVal = value;
        return this;
    }

    /**
     * Gets the {@link BooleanType} of the {@link BooleanOperator}
     *
     * @return Returns the {@link BooleanType} of the {@link BooleanOperator}
     */
    public BooleanType getValue() {
        return BooleanType.getBooleanTypeFromBooleanSymbol(this.operatorVal);
    }

    /**
     * Converts the boolean operator from a word to code
     *
     * @param operator The boolean operator as a word
     * @return Returns the boolean operator as code
     */
    private String findBooleanOperator(String operator) {
        if ("and".equals(operator)) {
            return BooleanType.AND.getBooleanSymbol();
        } else if ("or".equals(operator)){
            return BooleanType.OR.getBooleanSymbol();
        }

        return null;
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
