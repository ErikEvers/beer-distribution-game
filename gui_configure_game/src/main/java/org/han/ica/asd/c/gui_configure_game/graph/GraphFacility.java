package org.han.ica.asd.c.gui_configure_game.graph;

import java.util.ArrayList;
import java.util.List;

public abstract class GraphFacility {

    private int id = 0;
    ArrayList<GraphFacility> buyers;
    ArrayList<GraphFacility> suppliers;


     GraphFacility() {
        buyers = new ArrayList<>();
        suppliers = new ArrayList<>();
    }


     void removeSupplier(GraphFacility supplier) {
        suppliers.remove(supplier);
    }


    void addBuyer(GraphFacility supplier) {
        buyers.add(supplier);
    }

    void removeBuyer(GraphFacility buyer) {
        buyers.remove(buyer);
    }

    public List<GraphFacility> getBuyers() {
        return buyers;
    }

     List<GraphFacility> getSuppliers() {
        return suppliers;
    }

    public abstract String getColor();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
