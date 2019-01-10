package org.han.ica.asd.c.gui_configure_game.graph;

public class Factory extends GraphFacility {
    private static final String COLOR = "DODGERBLUE";


     void addBuyer(RegionalWarehouse buyer) {
        buyers.add(buyer);
    }

     void removeBuyer(RegionalWarehouse buyer) {
        buyers.remove(buyer);
    }

    public String getColor() {
        return COLOR;
    }

    @Override
    public String toString() {
        return "Factory";
    }
}
