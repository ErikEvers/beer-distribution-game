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
    BUDGET("budget");

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

    public boolean contains(String i){
        for (String s : value) {
            if(s.equals(i)){
                return true;
            }
        }
        return false;
    }
}
