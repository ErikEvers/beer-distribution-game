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
}
