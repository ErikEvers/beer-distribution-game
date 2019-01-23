package org.han.ica.asd.c.exceptions.communication;

import java.util.Map;

/**
 * This exception is thrown when something went wrong during the transactional sending of data.
 *
 * @author mathijs
 */
public class TransactionException extends Exception {

    Map<String, Object> exceptions;

    public TransactionException(String s, Map<String, Object> exceptions) {
        super(s);
        this.exceptions = exceptions;
    }

    @Override
    public String toString() {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(this.getMessage() + "\n");

        for (Map.Entry<String, Object> entry :exceptions.entrySet()){
            if (entry.getValue() instanceof SendGameMessageException){
                SendGameMessageException sendgameException = (SendGameMessageException) entry.getValue();
                errorMessage.append(entry.getKey() + ":\n");
                errorMessage.append(sendgameException.toString() + ":\n");
            }
            else if(entry.getValue() instanceof Exception){
                Exception exception = (Exception) entry.getValue();
                errorMessage.append(entry.getKey() + ":\n");
                errorMessage.append(exception.getMessage() + "\n");
            }
        }
        return errorMessage.toString();

    }
}
