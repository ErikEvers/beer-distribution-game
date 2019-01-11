package org.han.ica.asd.c.gui_configure_game.graph;

public class RegionalWarehouse extends GraphFacility {
    private static final String COLOR = "DODGERBLUE";


     void addBuyer(Wholesale buyer) {
        buyers.add(buyer);
    }

     void removeBuyer(Wholesale buyer) {
        buyers.remove(buyer);
    }

     void addSupplier(Factory supplier) {
        suppliers.add(supplier);
    }

     void removeSupplier(Factory supplier) {
        suppliers.remove(supplier);
    }

    public String getColor() {
        return COLOR;
    }

    @Override
    public String toString() {
        return "Regional Warehouse";
    }
}
