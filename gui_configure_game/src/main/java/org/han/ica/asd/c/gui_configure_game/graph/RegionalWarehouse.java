package org.han.ica.asd.c.gui_configure_game.graph;

public class RegionalWarehouse extends GraphFacility implements ISupplier<Wholesale>, IBuyer<Factory> {
    private static final String COLOR = "1fff34";


    public void addBuyer(Wholesale buyer) {
        buyers.add(buyer);
    }

    public void removeBuyer(Wholesale buyer) {
        buyers.remove(buyer);
    }

    public void addSupplier(Factory supplier) {
        suppliers.add(supplier);
    }

    public void removeSupplier(Factory supplier) {
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
