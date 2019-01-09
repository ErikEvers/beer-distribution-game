package org.han.ica.asd.c.exceptions.communication;

public class RoomException extends Exception{

    public RoomException(String message) {
        super(message);
    }

    public RoomException(Throwable exception){
        super(exception);
    }
}
