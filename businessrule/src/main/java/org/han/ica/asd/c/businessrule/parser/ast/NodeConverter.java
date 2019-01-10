package org.han.ica.asd.c.businessrule.parser.ast;

import com.google.inject.Inject;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeConverter {
    @Inject
    @Named("BusinessruleStore")
    private IBusinessRuleStore businessRuleStore;

    public NodeConverter() {
        //Empty constructor for Guice
    }

    /***
     * gets the facility id based on the given facility name
     * eg: "factory 1" gets the id of the first factory
     * @param facility
     * @return the identifier
     */
    public int getFacilityId(String facility) {
        List<List<String>> facilities = sortFacilities(businessRuleStore.getAllFacilities());

        String facilityId;

        if (facility.contains(FacilityType.FACTORY.getName())) {
            facilityId = facilities.get(FacilityType.FACTORY.getIndex()).get(separateFacilityId(facility));
        } else if (facility.contains(FacilityType.REGIONALWAREHOUSE.getName())) {
            facilityId = facilities.get(FacilityType.REGIONALWAREHOUSE.getIndex()).get(separateFacilityId(facility));
        } else if (facility.contains(FacilityType.WHOLESALER.getName())) {
            facilityId = facilities.get(FacilityType.WHOLESALER.getIndex()).get(separateFacilityId(facility));
        } else {
            facilityId = facilities.get(FacilityType.RETAILER.getIndex()).get(separateFacilityId(facility));
        }

        return Integer.parseInt(facilityId);
    }

    public List<List<String>> sortFacilities(List<List<String>> facilities){
        List<List<String>> returnList = new ArrayList<>();
        for (List<String> facility : facilities) {
            Collections.sort(facility);
            returnList.add(facility);
        }
        return returnList;
    }

    /***
     * Splits the facility name into the facility and the corresponding number
     * @param facility
     * @return the facility number
     */
    private int separateFacilityId(String facility) {
        String[] stringSplit = facility.split(" ");
        if (stringSplit.length > 1) {
            return Integer.parseInt(stringSplit[stringSplit.length-1]) - 1;
        }
        return 0;
    }
}
