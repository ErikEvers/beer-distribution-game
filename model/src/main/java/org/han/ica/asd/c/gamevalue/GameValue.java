package org.han.ica.asd.c.gamevalue;

public enum GameValue {
    STOCK("stock","inventory"),
    ORDERED("ordered"),
    OUTGOINGGOODS("outgoing goods"),
    BACKLOG("backlog","back orders"),
    INCOMINGORDER("incoming order"),
    HIGHEST("highest","biggest"),
    LOWEST("lowest","smallest"),
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
     * @param value the given value
     * @return
     */
    public boolean contains(String value){
        for (String s : this.value) {
            if(s.equals(value)){
                return true;
            }
        }
        return false;
    }
}
