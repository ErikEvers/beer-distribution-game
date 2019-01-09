package org.han.ica.asd.c.exceptions.communication;

public class DiscoveryException extends Exception{
    public DiscoveryException(String message) {
        super(message);
    }
    public DiscoveryException(Throwable exception){
        super(exception);
    }
}
