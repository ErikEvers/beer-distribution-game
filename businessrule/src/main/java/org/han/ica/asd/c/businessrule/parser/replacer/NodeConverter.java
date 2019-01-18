package org.han.ica.asd.c.businessrule.parser.replacer;

import com.google.inject.Inject;
import org.han.ica.asd.c.businessrule.parser.ast.action.ActionReference;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NodeConverter {
    @Inject
    private IBusinessRuleStore businessRuleStore;

    public static final int FIRST_FACILITY_ABOVE_BELOW = -1;
    private static final int FACILITY_NOT_FOUND = -404;

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

    private List<List<String>> sortFacilities(List<List<String>> facilities){
        return facilities.stream()
            .map(facility -> facility.stream()
                .sorted(Comparator.comparingInt(Integer::parseInt))
                .collect(Collectors.toList()))
            .collect(Collectors.toList());
    }

    /***
     * Splits the facility name into the facility and the corresponding number
     * @param facility
     * @return the facility number
     */
    private int separateFacilityId(String facility) {
        String[] stringSplit = facility.split(" ");
        if (stringSplit.length > 1) {
            return Integer.parseInt(stringSplit[stringSplit.length - 1]) - 1;
        }
        return 0;
    }

    public int getFacilityIdByAction(int ownFacilityId, ActionReference actionName) {
        List<List<String>> facilities = sortFacilities(businessRuleStore.getAllFacilities());

        for (int i = 0; i < facilities.size(); i++) {
            if(facilities.get(i).contains(String.valueOf(ownFacilityId))){
                return getFacilityIdByAction(actionName, i, ownFacilityId);
            }
        }

        return FACILITY_NOT_FOUND;
    }

    private int getFacilityIdByAction(ActionReference actionName, int facility , int ownFacilityId) {
        if("order".equals(actionName.getAction())){
            if(facility == FacilityType.FACTORY.getIndex()){
                return ownFacilityId;
            } else {
                return FIRST_FACILITY_ABOVE_BELOW;
            }
        } else {
            if(facility == FacilityType.RETAILER.getIndex()){
                return ownFacilityId;
            } else {
                return FIRST_FACILITY_ABOVE_BELOW;
            }
        }
    }
}
