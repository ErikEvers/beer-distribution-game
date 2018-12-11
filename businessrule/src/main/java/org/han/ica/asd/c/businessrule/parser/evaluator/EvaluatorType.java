package org.han.ica.asd.c.businessrule.parser.evaluator;

public enum EvaluatorType {
    LOWEST("lowest"),
    HIGHEST("highest"),
    DELIVER("deliver"),
    ABOVE("above"),
    BELOW("below");

    private String evaluatorSymbol;

    EvaluatorType(String evaluatorSymbol){
        this.evaluatorSymbol = evaluatorSymbol;
    }

    public String getEvaluatorSymbol(){
        return evaluatorSymbol;
    }
}
