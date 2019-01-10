package org.han.ica.asd.c.messagehandler.exceptions;

import java.util.ArrayList;

public class SendGameMessageException extends Exception {

    ArrayList<Exception> exceptions;

    public SendGameMessageException(String message){
        super(message);
    }

    public ArrayList<Exception> getExceptions() {
        return exceptions;
    }

    public void addException(Exception exception){
        exceptions.add(exception);
    }
}
