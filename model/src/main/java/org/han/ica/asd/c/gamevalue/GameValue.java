package org.han.ica.asd.c.gamevalue;

import java.util.Arrays;

public enum GameValue {
    STOCK("stock", "inventory"),
    ORDERED("ordered"),
    OUTGOINGGOODS("outgoing goods"),
    BACKLOG("backlog", "back orders"),
    INCOMINGORDER("incoming order"),
    HIGHEST("highest", "biggest"),
    LOWEST("lowest", "smallest"),
    FACTORY("factory"),
    REGIONALWAREHOUSE("regional warehouse"),
    WHOLESALER("wholesaler"),
    RETAILER("retailer"),
    BUDGET("budget");

    String[] value;

    /***
     * Sets the GameValue
     *
     * @param values the values of type String[]
     */
    GameValue(String... values) {
        this.value = (values);
    }

    /***
     * Gets the synonyms of the enum
     *
     * @return gets the synonyms of value
     */
    public String[] getValue() {
        return value;
    }

    /**
     * Checks if the given value is used in the list
     *
     * @param value the given value
     * @return
     * boolean value stating whether the given value is already in the String array
     */
    public boolean contains(String value) {
        return Arrays.stream(this.value).anyMatch(s -> s.contains(value));
    }

    /***
     * If the variable is a facility then it returns true
     *
     * @param variable one part of the value
     * @return the corresponding game value
     */
    public static boolean checkIfFacility(String variable) {
        int endOfFirstPart = variable.indexOf(' ');
        int notFound = -1;
        if (endOfFirstPart == notFound) {
            endOfFirstPart = variable.length();
        }
        for (GameValue gameValue : GameValue.values()) {
            if (!isFacility(gameValue)) {
                continue;
            }
            if (gameValue.contains(variable.substring(0, endOfFirstPart))) {
                return true;
            }
        }
        return false;
    }

    /***
     * Checks if the game value is of type facility
     *
     * @param gameValue the corresponding game value
     * @return if its a facility
     */
    private static boolean isFacility(GameValue gameValue) {
        return gameValue == GameValue.FACTORY ||
                gameValue == GameValue.WHOLESALER ||
                gameValue == GameValue.REGIONALWAREHOUSE ||
                gameValue == GameValue.RETAILER;
    }

    public static boolean isDisplayAttribute(String variable) {

        for (GameValue gameValue : GameValue.values()) {
            if (isFacility(gameValue) || gameValue == GameValue.INCOMINGORDER || gameValue == GameValue.HIGHEST || gameValue == GameValue.LOWEST) {
                continue;
            }
            if (gameValue.contains(variable)) {
                return true;
            }
        }

        return false;
    }
}
