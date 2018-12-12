package org.han.ica.asd.c.businessrule.parser.evaluator;

public class BusinessRuleException extends Exception {
    private final String errorMessage;
    private final int lineNumber;

    /**
     * Constructor
     * @param errorMessage Error message that explains what is wrong with the business rule
     * @param lineNumber Line number where the error took place
     */
    BusinessRuleException(String errorMessage, int lineNumber) {
        this.errorMessage = errorMessage;
        this.lineNumber = lineNumber;
    }

    /**
     * toString
     * @return Returns the error message together with where the error occurred
     */
    @Override
    public String toString() {
        return ("BusinessRuleException occurred on line " + lineNumber + ": " + errorMessage);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
