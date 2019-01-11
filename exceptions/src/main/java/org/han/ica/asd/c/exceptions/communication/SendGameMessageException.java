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
}
