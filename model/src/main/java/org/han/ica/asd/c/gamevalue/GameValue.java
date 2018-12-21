package org.han.ica.asd.c.gamevalue;

public enum GameValue {
    STOCK("stock", "inventory"),
    ORDERED("ordered"),
    OUTGOINGGOODS("outgoing goods"),
    BACKLOG("backlog", "back orders"),
    INCOMINGORDER("incoming order"),
    HIGHEST("highest", "biggest"),
    LOWEST("lowest", "smallest"),
    FACTORY("factory"),
    DISTIRBUTOR("distributor"),
    WHOLESALER("wholesaler"),
    RETAILER("retailer"),
    BUDGET("budget");

    String[] value;

    /***
     * sets the GameValue
     *
     * @param values the values of type String[]
     */
    GameValue(String... values) {
        this.value = (values);
    }

    /***
     * gets the synonyms of the enum
     * @return gets the synonyms of value
     */
    public String[] getValue() {
        return value;
    }

    /**
     * checks if the given value is used in the list
     *
     * @param value the given value
     * @return
     */
    public boolean contains(String value) {
        for (String s : this.value) {
            if (s.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /***
     * if the variable is a facility then it returns true
     * @param variable one part of the value
     * @return the corresponding game value
     */
    public static boolean checkIfFacility(String variable) {
        for (GameValue gameValue : GameValue.values()) {
            if (gameValue.contains(variable) && isFacility(gameValue)) {
                return true;
            }
        }
        return false;
    }

    /***
     * checks if the game value is of type facility
     * @param gameValue the corresponding game value
     * @return if its a facility
     */
    private static boolean isFacility(GameValue gameValue) {
        return gameValue == GameValue.FACTORY ||
                gameValue == GameValue.WHOLESALER ||
                gameValue == GameValue.DISTIRBUTOR ||
                gameValue == GameValue.RETAILER;
    }
}
