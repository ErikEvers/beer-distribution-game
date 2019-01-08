package org.han.ica.asd.c.gui_configure_game.graph;

public class Factory extends GraphFacility implements ISupplier<RegionalWarehouse> {
    private static final String COLOR = "DODGERBLUE";


    public void addBuyer(RegionalWarehouse buyer) {
        buyers.add(buyer);
    }

    public void removeBuyer(RegionalWarehouse buyer) {
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
