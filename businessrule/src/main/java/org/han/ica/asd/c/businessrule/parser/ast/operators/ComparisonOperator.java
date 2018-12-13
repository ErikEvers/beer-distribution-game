package org.han.ica.asd.c.businessrule.parser.ast.operators;

public class ComparisonOperator extends Operator {
    private String prefix = "ComO(";
    private String suffix = ")";

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
        if ("is".equals(operator) || "=".equals(operator) || "equal".equals(operator)) {
            return ComparisonType.EQUAL.getComparisonSymbol();
        } else if ("!=".equals(operator) || operator.contains("not equal")) {
            return ComparisonType.NOT.getComparisonSymbol();
        } else if (operator.contains("greater") || operator.contains("higher")) {
            return ComparisonType.GREATER.getComparisonSymbol();
        } else {
            return ComparisonType.LESS.getComparisonSymbol();
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
        stringBuilder.append(prefix).append(operatorVal).append(suffix);
    }
}
