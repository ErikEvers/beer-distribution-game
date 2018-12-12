package org.han.ica.asd.c.businessrule.parser;

import java.util.ArrayList;
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
            stringBuilder.append("Input syntax error occurred on line ").append(entry.getKey()).append(": ").append(entry.getValue()).append(".\n");
        }
        return stringBuilder.toString();
    }

    /***
     * Gets a list with all the lines that contains a error.
     * @return List with ints that stand for the lines with error's.
     */
    public List<Integer> getLinesWithError(){
        List<Integer> lines = new ArrayList<>();
        for (Map.Entry<Integer,String> entry : inputErrors.entrySet()) {
            lines.add(entry.getKey());
        }
        return lines;
    }
}
