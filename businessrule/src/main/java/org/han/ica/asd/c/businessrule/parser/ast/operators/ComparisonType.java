package org.han.ica.asd.c.businessrule.parser.ast.operators;

public enum ComparisonType {
    EQUAL("==", "=","equal","is"),
    NOT("!=","not equal"),
    GREATER(">", "greater", "higher" ),
    LESS("<","lower","less"),
    GREATER_EQUAL(">=", "greater than or equal to","higher than or equal to"),
    LESS_EQUAL("<=","less than or equal to","lower than or equal to");



    private String[] comparisonSymbol;

    /**
     * Constructor
     *
     * @param comparisonSymbol The comparison symbol
     */
    ComparisonType(String... comparisonSymbol) {
        this.comparisonSymbol = comparisonSymbol;
    }

    /**
     * Getter
     *
     * @return Returns the comparison symbol
     */
    public String[] get() {
        return comparisonSymbol;
    }

    public String get(int i) {
        return comparisonSymbol[i];
    }

    public boolean contains(String s){
        for (String symbol : comparisonSymbol) {
            if(symbol.contains(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the {@link ComparisonType} belonging to the given comparisonSymbol
     *
     * @param comparisonSymbol The comparisonSymbol representation of the {@link ComparisonType}
     * @return Returns the {@link BooleanType} belonging tot the given booleanSymbol
     */
    public static ComparisonType getComparisonTypeFromComparisonSymbol(String comparisonSymbol) {
        for (ComparisonType comparisonType : ComparisonType.values()) {
            for (String i:comparisonType.comparisonSymbol) {
                if (i.equalsIgnoreCase(comparisonSymbol)) {
                    return comparisonType;
                }
            }
        }
        return null;
    }

}
