package org.han.ica.asd.c.gui_configure_game.graph;

public class Wholesale extends GraphFacility {
    private static final String COLOR = "#ffbc1f";

     void addBuyer(Retailer buyer) {
        buyers.add(buyer);
    }

     void removeBuyer(Retailer buyer) {
        buyers.remove(buyer);
    }

     void addSupplier(RegionalWarehouse supplier) {
        suppliers.add(supplier);
    }

     void removeSupplier(RegionalWarehouse supplier) {
        suppliers.remove(supplier);
    }

    public String getColor() {
        return COLOR;
    }


    @Override
    public String toString() {
        return "Wholesale";
    }
}
