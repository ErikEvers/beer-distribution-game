package org.han.ica.asd.c.businessrule.parser.ast.operators;

public class ComparisonOperator extends Operator {
    private static final String PREFIX = "ComO(";

    /**
     * Constructor
     */
    public ComparisonOperator() {
    }

    /**
     * Constructor
     *
     * @param operator The comparison operator as a word
     */
    public ComparisonOperator(String operator) {
        this.operatorVal = findComparisonOperator(operator);
    }

    /**
     * Sets the operatorValue of the {@link Operator}
     *
     * @param value The operatorValue to add
     * @return Returns the instance of the {@link ComparisonOperator}
     */
    @Override
    public ComparisonOperator addValue(String value) {
        operatorVal = value;
        return this;
    }

    /**
     * Converts the comparison operator from a word to code
     *
     * @param operator The comparison operator as a word
     * @return The comparison operator as code
     */
    private String findComparisonOperator(String operator) {
        if (ComparisonType.GREATER_EQUAL.contains(operator)) {
            return ComparisonType.GREATER_EQUAL.get(0);
        } else if (ComparisonType.LESS_EQUAL.contains(operator)) {
            return ComparisonType.LESS_EQUAL.get(0);
        } else if (ComparisonType.EQUAL.contains(operator)) {
            return ComparisonType.EQUAL.get(0);
        } else if (ComparisonType.NOT.contains(operator)) {
            return ComparisonType.NOT.get(0);
        } else if (ComparisonType.GREATER.contains(operator)) {
            return ComparisonType.GREATER.get(0);
        } else {
            return ComparisonType.LESS.get(0);
        }
    }

    /**
     * Gets the value of the ComparisonOperator
     *
     * @return Returns the {@link ComparisonType} of the {@link ComparisonOperator}
     */
    public ComparisonType getValue() {
        return ComparisonType.getComparisonTypeFromComparisonSymbol(this.operatorVal);
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
        stringBuilder.append(PREFIX).append(operatorVal).append(SUFFIX);
    }
}
