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

    /**
     * Gets the {@link BooleanType} belonging to the given booleanSymbol
     *
     * @param booleanSymbol The booleanSymbol representation of the {@link BooleanType}
     * @return Returns the {@link BooleanType} belonging tot the given booleanSymbol
     */
    public static BooleanType getBooleanTypeFromBooleanSymbol(String booleanSymbol) {
        for (BooleanType booleanType : BooleanType.values()) {
            if (booleanType.booleanSymbol.equalsIgnoreCase(booleanSymbol)) {
                return booleanType;
            }
        }
        return null;
    }
}
