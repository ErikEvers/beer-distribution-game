package org.han.ica.asd.c.exceptions.gameleader;

public class FacilityNotAvailableException extends Exception {
    FacilityNotAvailableException() {
        super();
    }

    FacilityNotAvailableException(String message) {
        super(message);
    }

    FacilityNotAvailableException(Throwable cause) {
        super(cause);
    }

    FacilityNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
