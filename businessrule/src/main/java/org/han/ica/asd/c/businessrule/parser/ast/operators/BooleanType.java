package org.han.ica.asd.c.businessrule.parser.ast.operators;

public enum BooleanType {
    AND("&&"),
    OR("||");


    private String booleanSymbol;

    BooleanType(String booleanSymbol) {
        this.booleanSymbol = booleanSymbol;
    }

    public String getBooleanSymbol() {
        return booleanSymbol;
    }

    public static BooleanType getBooleanTypeFromBooleanSymbol(String booleanSymbol) {
        for (BooleanType booleanType : BooleanType.values()) {
            if (booleanType.booleanSymbol.equalsIgnoreCase(booleanSymbol)) {
                return booleanType;
            }
        }
        return null;
    }
}
