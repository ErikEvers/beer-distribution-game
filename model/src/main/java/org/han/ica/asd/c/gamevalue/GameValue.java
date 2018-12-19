package org.han.ica.asd.c.gamevalue;

import org.han.ica.asd.c.model.Facility;

import java.util.Arrays;
import java.util.List;

public enum GameValue {
    INVENTORY("inventory"), OPENORDER("open order"),
    ROUND("round"), STOCK("stock"), ORDER("order"),
    OUTGOINGGOODS("outgoinggoods"), BACKORDERS("backorders"),
    BACKLOG("backlog"),INCOMINGORDER("incoming order"),
    HIGHEST("highest","biggest"),LOWEST("lowest","smallest"),
    Facility("factory","distributor","wholesaler","retailer"),BELOW("below"),ABOVE("above");

    String[] value;

    /***
     * sets the GameValue
     *
     * @param values
     */
    GameValue(String... values) {
        this.value = (values);
    }

    public String[] getValue() {
        return value;
    }
}
