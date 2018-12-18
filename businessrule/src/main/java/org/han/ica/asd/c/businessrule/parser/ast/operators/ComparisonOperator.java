package org.han.ica.asd.c.businessrule.parser.ast.operators;

public class ComparisonOperator extends Operator {
    private static final String prefix = "ComO(";

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
        if (ComparisonType.GREATER_EQUAL.get().equals(operator) || (operator.contains(ComparisonType.EQUAL_CHARS.get()) && (operator.contains(ComparisonType.GREATER_CHARS_HIGHER.get()) || operator.contains(ComparisonType.GREATER_CHARS.get())))) {
            return ComparisonType.GREATER_EQUAL.get();
        } else if (ComparisonType.LESS_EQUAL.get().equals(operator) || (operator.contains(ComparisonType.EQUAL_CHARS.get()) && (operator.contains(ComparisonType.LESS_CHARS.get()) || operator.contains(ComparisonType.LESS_CHARS_LOWER.get())))) {
            return ComparisonType.LESS_EQUAL.get();
        } else if (ComparisonType.EQUAL_CHARS_IS.get().equals(operator) || ComparisonType.EQUAL_ONE.get().equals(operator) || operator.contains(ComparisonType.EQUAL_CHARS.get())) {
            return ComparisonType.EQUAL.get();
        } else if (ComparisonType.NOT.get().equals(operator) || operator.contains(ComparisonType.NOT_CHARS.get())) {
            return ComparisonType.NOT.get();
        } else if (operator.contains(ComparisonType.GREATER_CHARS.get()) || operator.contains(ComparisonType.GREATER_CHARS_HIGHER.get())) {
            return ComparisonType.GREATER.get();
        } else {
            return ComparisonType.LESS.get();
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
