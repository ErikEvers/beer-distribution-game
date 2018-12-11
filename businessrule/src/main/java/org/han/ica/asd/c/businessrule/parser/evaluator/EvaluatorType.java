package org.han.ica.asd.c.businessrule.parser.evaluator;

public enum EvaluatorType {
    LOWEST("lowest"),
    HIGHEST("highest"),
    DELIVER("deliver"),
    ABOVE("above"),
    BELOW("below");

    private String evaluatorSymbol;

    /**
     * Constructor
     * @param evaluatorSymbol The evaluator symbol
     */
    EvaluatorType(String evaluatorSymbol){
        this.evaluatorSymbol = evaluatorSymbol;
    }

    /**
     * Getter
     * @return Returns the evaluator symbol
     */
    public String getEvaluatorSymbol(){
        return evaluatorSymbol;
    }
}
