package org.han.ica.asd.c.exceptions;

public class FacilityNotFoundException extends Exception {
    public FacilityNotFoundException() {
        super("The given facility was not found!");
    }
}
