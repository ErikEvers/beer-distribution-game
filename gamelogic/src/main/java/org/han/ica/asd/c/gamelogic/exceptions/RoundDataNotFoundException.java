package org.han.ica.asd.c.gamelogic.exceptions;

public class RoundDataNotFoundException extends Exception {
    public RoundDataNotFoundException() {
        super("There was no round data found with the given facility!");
    }
}
