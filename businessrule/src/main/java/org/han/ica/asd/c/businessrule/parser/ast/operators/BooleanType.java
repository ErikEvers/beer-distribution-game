package org.han.ica.asd.c.businessrule.parser.ast.operators;

public enum BooleanType {
    AND("&&"),
    OR("||");


    private String booleanSymbol;

    BooleanType(String booleanSymbol){
        this.booleanSymbol = booleanSymbol;
    }

    public String getBooleanSymbol(){
        return booleanSymbol;
    }
}
