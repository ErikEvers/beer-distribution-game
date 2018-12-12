package org.han.ica.asd.c.businessrule.parser;

public class UserInputBusinessRule {
    private String businessRule;
    private String errorMessage;
    private int lineNumber;

    public UserInputBusinessRule(String businessRule,int lineNumber){
        this.businessRule = businessRule;
        this.lineNumber = lineNumber;
    }

    public boolean HasError(){
        return !errorMessage.isEmpty();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getBusinessRule() {
        return businessRule;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
