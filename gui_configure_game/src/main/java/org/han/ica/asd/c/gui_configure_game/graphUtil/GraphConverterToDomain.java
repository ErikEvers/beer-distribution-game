package org.han.ica.asd.c.gui_configure_game.graphUtil;

import org.han.ica.asd.c.gui_configure_game.graph.*;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityType;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterables.isEmpty;


public class GraphConverterToDomain {

    private Graph graph;
    private List<Facility> facilities = new ArrayList<>();

    private Provider<Facility> facilityProvider;
    private Provider<FacilityType> facilityTypeProvider;

    @Inject
    public GraphConverterToDomain(Provider<Facility> facilityProvider, Provider<FacilityType> facilityTypeProvider) {
        this.facilityProvider = facilityProvider;
        this.facilityTypeProvider = facilityTypeProvider;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        this.facilities = allToList();
    }

    /**
     * converts a Graphfacility to its domain variant. includes the Id
     *
     * @param graphFacility facility to be converted
     * @return returns a domain type facility
     */
    private Facility facilityToDomain(GraphFacility graphFacility) {

        Facility facility = facilityProvider.get();
        FacilityType facilityType = facilityTypeProvider.get();
        facility.setFacilityId(graphFacility.getId());

        if (graphFacility instanceof Factory) {
            facilityType.setFacilityName("Factory");
        }
        if (graphFacility instanceof Wholesale) {
            facilityType.setFacilityName("Wholesaler");
        }
        if (graphFacility instanceof RegionalWarehouse) {
            facilityType.setFacilityName("Regional Warehouse");
        }
        if (graphFacility instanceof Retailer) {
            facilityType.setFacilityName("Retailer");
        }
        facility.setFacilityType(facilityType);
        return facility;
    }

    /**
     * fetches all the Facility's out of the GUI graph
     *
     * @return A list of (domain type) facilities
     */
    public List<Facility> allToList() {
        List<Facility> facilities = new ArrayList<>();
        for (Object graphFacility : graph.getFacilities()) {
            facilities.add(facilityToDomain((GraphFacility) graphFacility));
        }
        return facilities;
    }

    /**
     * converts the GUI graph to  a Graph with the datastrucutre of the domain
     *
     * @return Map<Facility                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                               List                                                                                                                                                                                                                                                               <                                                                                                                                                                                                                                                               Facility>
     */
    public Map<Facility, List<Facility>> convertToDomeinGraph() {

        Map<Facility, List<Facility>> facilitiesLinkedTo = new HashMap<>();

        for (GraphFacility graphFacility : graph.getFacilities()) {
            if (!graphFacility.toString().equals("Factory")) {
                List<Facility> childs = new ArrayList<>();
                Facility parent = facilityToDomain(graphFacility);
                if (!isEmpty(graphFacility.getSuppliers())) {
                    for (Object currentChild : graphFacility.getSuppliers()) {
                        childs.add(facilityToDomain((GraphFacility) currentChild));
                    }
                }

                facilitiesLinkedTo.put(parent, childs);
            }
        }
        return facilitiesLinkedTo;
    }

    /**
     * counts all the facilities in a GUI graph
     *
     * @return int array of facilities where  [0] = Factory, [1] = Regional Wharehouse ,  [2] = Wholsaler [3] = Retailer
     */
    public int[] countAll() {

        int factoryCount = 0;
        int wholesaleCount = 0;
        int regionalWharehouseCount = 0;
        int retailerCount = 0;


        for (Object current : graph.getFacilities()) {
            if (current instanceof Factory) {
                factoryCount++;
            }
            if (current instanceof RegionalWarehouse) {
                regionalWharehouseCount++;
            }
            if (current instanceof Wholesale) {
                wholesaleCount++;
            }
            if (current instanceof Retailer) {
                retailerCount++;
            }
        }

        return new int[]{factoryCount, regionalWharehouseCount, wholesaleCount, retailerCount};
    }

}
