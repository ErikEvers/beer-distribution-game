package org.han.ica.asd.c.gui_configure_game.graphUtil;


import org.han.ica.asd.c.gui_configure_game.graph.*;

import java.util.List;

import static com.google.common.collect.Iterables.isEmpty;

public class GraphToFacilityChecker {

    /**
     * validates a graph if all the nodes are connected to atleast 1 other node
     *
     * @param index size of the graph
     * @param graph graph to be checked
     * @return true of false according to the check
     */
    public Boolean graphChecker(List<GraphFacility> graphFacilityList) {
        for (GraphFacility currentFacility : graphFacilityList) {
            System.out.println("currentfacility = " + currentFacility.toString());
            if (currentFacility instanceof RegionalWarehouse || currentFacility instanceof Wholesale) {
                if (!isConnectedBuyer(currentFacility) || !isConnectedSupplier(currentFacility)) {
                    return false;
                }
            }
            if (currentFacility instanceof Factory) {
                if (!isConnectedBuyer(currentFacility)) {
                    return false;
                }
            }
            if (currentFacility instanceof Retailer) {
                if (!isConnectedSupplier(currentFacility)) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Checks if one of everything exists in the graph
     *
     * @param graph to be checked
     * @return true of false according to the check
     */
    public Boolean oneOfEverything(Graph graph) {

        boolean factoryAvailable = false;
        boolean wholesaleAvailable = false;
        boolean regionalWarehouseAvailable = false;
        boolean retailAvailable = false;

        if (graph.getFacilities().size() < 4) {
            return false;
        } else {
            for (GraphFacility current : graph.getFacilities()) {
                if (current instanceof Factory) {
                    factoryAvailable = true;
                }
                if (current instanceof Wholesale) {
                    wholesaleAvailable = true;
                }
                if (current instanceof RegionalWarehouse) {
                    regionalWarehouseAvailable = true;
                }
                if (current instanceof Retailer) {
                    retailAvailable = true;
                }
            }
        }
        return (factoryAvailable && wholesaleAvailable && regionalWarehouseAvailable && retailAvailable);
    }

    private boolean isConnectedBuyer(GraphFacility currentFacility) {
        return (!isEmpty(currentFacility.getBuyers()));
    }

    private boolean isConnectedSupplier(GraphFacility currentFacility) {
        return (!isEmpty(currentFacility.getSuppliers()));
    }

}
