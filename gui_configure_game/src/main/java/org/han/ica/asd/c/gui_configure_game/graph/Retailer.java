package org.han.ica.asd.c.gui_configure_game.graph;

public class Retailer extends GraphFacility  {
    private static final String COLOR = "ff2151";


     void addSupplier(Wholesale supplier) {
        suppliers.add(supplier);
    }

     void removeSupplier(Wholesale supplier) {
        suppliers.remove(supplier);
    }

    public String getColor() {
        return COLOR;
    }

    @Override
    public String toString(){
        return "Retailer";
    }
}
