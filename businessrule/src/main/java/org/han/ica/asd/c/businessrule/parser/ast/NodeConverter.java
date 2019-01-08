package org.han.ica.asd.c.businessrule.parser.ast;

import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;

import java.util.ArrayList;
import java.util.List;

public class NodeConverter {
    //@Inject
    private IBusinessRuleStore businessRuleStore;

    /***
     * gets the facility id based on the given facility name
     * eg: "factory 1" gets the id of the first factory
     * @param facility
     * @return the identifier
     */
    public int getFacilityId(String facility) {
        // TO-DO: List<List<String>> facilities = businessRuleStore.getAllFacilities()
        // This is a mock of the function above
        List<List<String>> facilities = new ArrayList<>();
        List<String> factory = new ArrayList<>();
        factory.add("1");
        factory.add("2");
        List<String> distributor = new ArrayList<>();
        distributor.add("3");
        distributor.add("4");
        List<String> wholesaler = new ArrayList<>();
        wholesaler.add("5");
        List<String> retailer = new ArrayList<>();
        retailer.add("6");
        facilities.add(factory);
        facilities.add(distributor);
        facilities.add(wholesaler);
        facilities.add(retailer);

        String facilityId;

        if (facility.contains(FacilityType.FACTORY.getName())) {
            facilityId = facilities.get(FacilityType.FACTORY.getIndex()).get(separateFacilityId(facility));
        } else if (facility.contains(FacilityType.DISTRIBUTOR.getName())) {
            facilityId = facilities.get(FacilityType.DISTRIBUTOR.getIndex()).get(separateFacilityId(facility));
        } else if (facility.contains(FacilityType.WHOLESALER.getName())) {
            facilityId = facilities.get(FacilityType.WHOLESALER.getIndex()).get(separateFacilityId(facility));
        } else {
            facilityId = facilities.get(FacilityType.RETAILER.getIndex()).get(separateFacilityId(facility));
        }

        return Integer.parseInt(facilityId);
    }

    /***
     * Splits the facility name into the facility and the corresponding number
     * @param facility
     * @return the facility number
     */
    private int separateFacilityId(String facility) {
        String[] stringSplit = facility.split(" ");
        if (stringSplit.length > 1) {
            return Integer.parseInt(stringSplit[1]) - 1;
        }
        return 0;
    }
}
