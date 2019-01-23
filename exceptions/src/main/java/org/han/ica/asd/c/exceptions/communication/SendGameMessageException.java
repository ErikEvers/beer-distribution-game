package org.han.ica.asd.c.exceptions.communication;

import java.util.ArrayList;
import java.util.List;

public class SendGameMessageException extends Exception {

    private final List<Exception> exceptions;

    public SendGameMessageException(String message){
        super(message);
        exceptions = new ArrayList<>();
    }

    public List<Exception> getExceptions() {
        return exceptions;
    }

    public void addException(Exception exception){
        exceptions.add(exception);
    }

    @Override
    public String toString() {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(this.getMessage());
        errorMessage.append("\n");
        errorMessage.append("Errors:\n");
        for (Exception innerException : this.getExceptions()) {
            errorMessage.append(innerException.getMessage());
            errorMessage.append("\n");
        }
        return errorMessage.toString();
    }
}
