package org.han.ica.asd.c.exceptions.communication;

/**
 * Is used for throwing a custom exception when the current node has no active leader. Mainly thrown in the 'MessageProcessor'.
 *
 * @author oscar
 */
public class LeaderNotPresentException extends Exception {

    public LeaderNotPresentException(String s) {
        super(s);
    }
}

