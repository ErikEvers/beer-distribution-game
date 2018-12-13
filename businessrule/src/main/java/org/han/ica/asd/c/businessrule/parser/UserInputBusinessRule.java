package org.han.ica.asd.c.businessrule.parser;

public class UserInputBusinessRule {
    private String businessRule;
    private String errorMessage;
    private int lineNumber;

    public UserInputBusinessRule(String businessRule, int lineNumber) {
        this.businessRule = businessRule;
        this.lineNumber = lineNumber;
    }

    public boolean hasError() {
        return errorMessage != null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error of a UserBusinessRule, If there is more than one error than add to that.
     *
     * @param errorMessage The message that needs to be set.
     */
    public void setErrorMessage(String errorMessage) {
        if (this.errorMessage != null) {
            this.errorMessage += " & " + errorMessage;
        } else {
            this.errorMessage = errorMessage;
        }
    }

    public String getBusinessRule() {
        return businessRule;
    }

    public void setBusinessRule(String businessRule) {
        this.businessRule = businessRule;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
