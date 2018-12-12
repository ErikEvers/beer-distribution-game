package org.han.ica.asd.c.businessrule.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInputException extends Exception {
    private Map<Integer, String> inputErrors = new HashMap<>();

    public UserInputException(List<BusinessRuleException> exceptions) {
        for (BusinessRuleException exception : exceptions) {
            inputErrors.put(exception.getLineNumber(), exception.getErrorMessage());
        }
    }

    /**
     * toString
     * @return Returns the error message together with where the error occurred
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Integer,String> entry : inputErrors.entrySet()) {
            stringBuilder.append("Input syntax error: ").append(entry.getValue()).append(" on line ").append(entry.getKey()).append(".\n");
        }
        return stringBuilder.toString();
    }
}
