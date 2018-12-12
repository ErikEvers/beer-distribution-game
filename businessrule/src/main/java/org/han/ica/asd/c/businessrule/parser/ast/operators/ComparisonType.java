package org.han.ica.asd.c.businessrule.parser.ast.operators;

public enum ComparisonType {
    EQUAL("=="),
    NOT("!="),
    GREATER(">"),
    LESS("<");


    private String comparisonSymbol;

    ComparisonType(String comparisonSymbol){
        this.comparisonSymbol = comparisonSymbol;
    }

    public String getComparisonSymbol(){
        return comparisonSymbol;
    }

    public static ComparisonType getComparisonTypeFromComparisonSymbol(String comparisonSymbol) {
        for (ComparisonType comparisonType : ComparisonType.values()) {
            if (comparisonType.comparisonSymbol.equalsIgnoreCase(comparisonSymbol)) {
                return comparisonType;
            }
        }
        return null;
    }

}
