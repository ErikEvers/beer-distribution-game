package org.han.ica.asd.c.gamevalue;

public enum GAME_VALUE {
    INVENTORY("inventory"), OPENORDER("open order"),
    ROUND("round"), STOCK("stock"), ORDER("order"),
    OUTGOINGGOODS("outgoinggoods"), BACKORDERS("back orders");
    String value;

    /***
     * sets the gamevalue
     * @param value
     */
    GAME_VALUE(String value) {
        this.value = value;
    }

    /***
     * gets the gameValue and returns the matching string
     * @param value
     * @return
     */
    public static GAME_VALUE getGameValue(String value) {
        switch (value) {
            case "inventory":
                return INVENTORY;
            case "open order":
                return OPENORDER;
            case "round":
                return ROUND;
            case"stock":
                return STOCK;
            case "order":
                return ORDER;
            case "outgoinggoods":
                return OUTGOINGGOODS;
            case "back orders":
                return BACKORDERS;
            default:
                return null;
        }
    }
}
