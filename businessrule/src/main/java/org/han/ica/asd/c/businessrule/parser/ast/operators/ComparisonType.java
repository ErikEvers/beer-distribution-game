package org.han.ica.asd.c.businessrule.parser.ast.operators;

public enum ComparisonType {
    EQUAL("=="),
    NOT("!="),
    GREATER(">"),
    LESS("<");


    private String comparisonSymbol;

    /**
     * Constructor
     *
     * @param comparisonSymbol The comparison symbol
     */
    ComparisonType(String comparisonSymbol) {
        this.comparisonSymbol = comparisonSymbol;
    }

    /**
     * Getter
     *
     * @return Returns the comparison symbol
     */
    public String getComparisonSymbol() {
        return comparisonSymbol;
    }

    /**
     * Gets the {@link ComparisonType} belonging to the given comparisonSymbol
     *
     * @param comparisonSymbol The comparisonSymbol representation of the {@link ComparisonType}
     * @return Returns the {@link BooleanType} belonging tot the given booleanSymbol
     */
    public static ComparisonType getComparisonTypeFromComparisonSymbol(String comparisonSymbol) {
        for (ComparisonType comparisonType : ComparisonType.values()) {
            if (comparisonType.comparisonSymbol.equalsIgnoreCase(comparisonSymbol)) {
                return comparisonType;
            }
        }
        return null;
    }

}
