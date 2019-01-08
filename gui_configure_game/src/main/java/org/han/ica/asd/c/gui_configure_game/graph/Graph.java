package org.han.ica.asd.c.gui_configure_game.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    ArrayList<GraphFacility> facilities;

    public Graph() {
        facilities = new ArrayList<GraphFacility>();
    }

    public void addEdge(ISupplier parent, IBuyer child) throws GraphException {
        if (facilities.contains(parent) && facilities.contains(child)) {
            parent.addBuyer(child);
            child.addSupplier(parent);
        } else {
            throw new GraphException("One or more facilities have not yet been added to the facility list.");
        }
    }

    public void removeEdge(ISupplier parent, IBuyer child) {
        parent.removeBuyer(child);
        child.removeSupplier(parent);
    }

    public void addFacility(GraphFacility graphFacility) {
        facilities.add(graphFacility);
    }

    public void removeFacility(GraphFacility graphFacility) {
        for (GraphFacility f : facilities) {
            if (f.getBuyers().contains(graphFacility)) {
                ((ISupplier) f).removeBuyer(graphFacility);
            }

            if (f.getSuppliers().contains(graphFacility)) {
                ((IBuyer) f).removeSupplier(graphFacility);
            }
        }

        facilities.remove(graphFacility);
    }

    public ArrayList<GraphFacility> getFacilities() {
        return facilities;
    }
}
