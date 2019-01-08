package org.han.ica.asd.c.model.interface_models;

import java.util.HashMap;
import java.util.Map;

public class UserInputBusinessRule {
    private String businessRule;
    private String errorMessage;
    private int lineNumber;
    private Map<Integer,Integer> errorWord = new HashMap<>();

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

    public Map<Integer, Integer> getErrorWord() {
        return errorWord;
    }

    public void setErrorWord(int beginChar, int endChar) {
        this.errorWord.put(beginChar,endChar);
    }
}
