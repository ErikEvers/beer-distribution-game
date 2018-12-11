package org.han.ica.asd.c.businessrule.parser.ast.operators;

public class CalculationOperator extends Operator {
    private String prefix = "CalO(";
    private String suffix = ")";

    /**
     * Constructor
     * @param operator The calculation operator
     */
    public CalculationOperator(String operator) {
        this.operatorVal = operator;
    }

    /**
     * Calls the equals function of its super class
     * @param o Object that needs to be checked if it's equal to this object
     * @return Returns true or false depending on if it's equal or not
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * Calls the hashCode function of its super class
     * @return Returns the hashcode
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Encodes the parsed tree in a single string so that it can be stored in the database
     * @param stringBuilder Stringbuilder that is used to encode the tree
     */
    @Override
    public void encode(StringBuilder stringBuilder) {
        stringBuilder.append(prefix).append(operatorVal).append(suffix);
    }
}
