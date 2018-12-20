package org.han.ica.asd.c.messagehandler.exceptions;

/**
 * Is used for throwing a custom exception when the current node has no active leader. Mainly thrown in the 'MessageProcessor'.
 *
 * @author oscar
 * @see org.han.ica.asd.c.messagehandler.MessageProcessor
 */
public class LeaderNotPresentException extends Exception {

    public LeaderNotPresentException() {
        super();
    }

    public LeaderNotPresentException(Throwable cause) {
        super(cause);
    }

    public LeaderNotPresentException(String s) {
        super(s);
    }
}

