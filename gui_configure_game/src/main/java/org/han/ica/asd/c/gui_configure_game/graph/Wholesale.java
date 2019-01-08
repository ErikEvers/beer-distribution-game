package org.han.ica.asd.c.gui_configure_game.graph;

public class Wholesale extends GraphFacility implements ISupplier<Retailer>, IBuyer<RegionalWarehouse> {
    private static final String COLOR = "#ffbc1f";

    public void addBuyer(Retailer buyer) {
        buyers.add(buyer);
    }

    public void removeBuyer(Retailer buyer) {
        buyers.remove(buyer);
    }

    public void addSupplier(RegionalWarehouse supplier) {
        suppliers.add(supplier);
    }

    public void removeSupplier(RegionalWarehouse supplier) {
        suppliers.remove(supplier);
    }

    public String getColor() {
        return COLOR;
    }


    @Override
    public String toString(){
        return "Wholesale";
    }
}
