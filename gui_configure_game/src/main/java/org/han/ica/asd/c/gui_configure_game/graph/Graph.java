package org.han.ica.asd.c.gui_configure_game.graph;

import java.util.ArrayList;
import java.util.List;


public class Graph {

    ArrayList<GraphFacility> facilities;

    Graph() {
        facilities = new ArrayList<>();
    }


    /**
     * adds childs to a parent.
     * @param parent parent node
     * @param child child node
     * @throws GraphException no facility detected in the list
     */
    public void addEdge(GraphFacility parent, GraphFacility child) throws GraphException {
        if (facilities.contains( parent) && facilities.contains(child)) {
            for (int i = 0; i < facilities.size(); i++) {
                if (facilities.get(i).getId() == parent.getId()) {
                    facilities.get(i).addBuyer(child);
                }
            }
        } else {
            throw new GraphException("One or more facilities have not yet been added to the facility list.");
        }
    }

    /**
     * removes edge from the graph
     * @param parent parent node
     * @param child child node
     */
    void removeEdge(GraphFacility parent, GraphFacility child) {
        parent.removeBuyer(child);
        child.removeSupplier(parent);
    }

    public void removeChild(GraphFacility parent, GraphFacility child) {
        if (facilities.contains(parent) && facilities.contains(child)) {
            for (int i = 0; i < facilities.size(); i++) {
                if (facilities.get(i).getId() == parent.getId()) {
                    facilities.get(i).removeBuyer(child);
                }
            }
        }
    }
    public void addFacility(GraphFacility graphFacility) {
        facilities.add(graphFacility);
    }

    public void removeFacility(GraphFacility graphFacility) {
        for (GraphFacility f : facilities) {
            if (f.getBuyers().contains(graphFacility)) {
                f.removeBuyer(graphFacility);
            }

            if (f.getSuppliers().contains(graphFacility)) {
                f.removeSupplier(graphFacility);
            }
        }

        facilities.remove(graphFacility);
    }

    public List<GraphFacility> getFacilities() {
        return facilities;
    }
}
