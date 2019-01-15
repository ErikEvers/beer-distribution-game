package org.han.ica.asd.c.exceptions.communication;

/**
 * This exception is thrown when something went wrong during the transactional sending of data.
 *
 * @author mathijs
 */
public class TransactionException extends Exception {

    public TransactionException(String s) {
        super(s);
    }
}
