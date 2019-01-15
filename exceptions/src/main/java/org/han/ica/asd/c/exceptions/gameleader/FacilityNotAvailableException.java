package org.han.ica.asd.c.exceptions.gameleader;

public class FacilityNotAvailableException extends Exception {
    public FacilityNotAvailableException() {
        super();
    }

    public FacilityNotAvailableException(String message) {
        super(message);
    }

    public FacilityNotAvailableException(Throwable cause) {
        super(cause);
    }

    public FacilityNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
