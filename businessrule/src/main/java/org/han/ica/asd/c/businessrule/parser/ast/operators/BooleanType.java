package org.han.ica.asd.c.businessrule.parser.ast.operators;

public enum BooleanType {
    AND("&&"),
    OR("||");


    private String booleanSymbol;

    /**
     * Constructor
     *
     * @param booleanSymbol The boolean symbol
     */
    BooleanType(String booleanSymbol) {
        this.booleanSymbol = booleanSymbol;
    }

    /**
     * Getter
     *
     * @return Returns the boolean symbol
     */
    public String getBooleanSymbol() {
        return booleanSymbol;
    }
}
