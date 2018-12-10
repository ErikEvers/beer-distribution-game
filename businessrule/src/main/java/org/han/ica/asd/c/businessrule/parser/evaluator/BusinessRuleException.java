package org.han.ica.asd.c.businessrule.parser.evaluator;

public class BusinessRuleException extends Exception {
    private final String errorMessage;
    private final int lineNumber;

    BusinessRuleException(String errorMessage, int lineNumber) {
        this.errorMessage = errorMessage;
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return ("BusinessRuleException occurred on line " + lineNumber + ": " + errorMessage);
    }
}
