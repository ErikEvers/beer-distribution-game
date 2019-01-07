package org.han.ica.asd.c.gui_configure_game.graph;

import java.util.ArrayList;
import java.util.List;

public abstract class GraphFacility {
    ArrayList<IBuyer> buyers;
    ArrayList<ISupplier> suppliers;


    public GraphFacility() {
        buyers = new ArrayList<IBuyer>();
        suppliers = new ArrayList<ISupplier>();
    }

    public List getBuyers() {
        return buyers;
    }

    public List getSuppliers() {
        return suppliers;
    }

    public abstract String getColor();
}
