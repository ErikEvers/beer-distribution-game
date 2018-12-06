package org.han.ica.asd.c.businessrule.evaluator;

public class BusinessRuleException extends Exception {
    private final String ERRORMESSAGE;
    private final int LINENUMBER;

    BusinessRuleException(String errorMessage, int lineNumber) {
        this.ERRORMESSAGE = errorMessage;
        this.LINENUMBER = lineNumber;
    }

    @Override
    public String toString() {
        return ("BusinessRuleException occurred on line " + LINENUMBER + ": " + ERRORMESSAGE);
    }
}
