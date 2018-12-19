package org.han.ica.asd.c.discovery;

public class DiscoveryException extends Exception{
    public DiscoveryException(String message) {
        super(message);
    }
    public DiscoveryException(Throwable exception){
        super(exception);
    }
}
