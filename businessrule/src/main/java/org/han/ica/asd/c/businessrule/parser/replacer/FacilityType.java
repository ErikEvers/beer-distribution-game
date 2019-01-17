package org.han.ica.asd.c.businessrule.parser.replacer;

import java.util.AbstractMap;
import java.util.Map;

public enum FacilityType {
    FACTORY("factory", 0),
    REGIONALWAREHOUSE("regional warehouse", 1),
    WHOLESALER("wholesaler", 2),
    RETAILER("retailer", 3);

    private Map.Entry<String, Integer> facility;

    /**
     * Constructor
     *
     * @param facilityString Name of the facility
     * @param facilityInt Number that corresponds to the facility
     */
    FacilityType(String facilityString, int facilityInt) {
        facility = new AbstractMap.SimpleEntry<>(facilityString, facilityInt);
    }

    /**
     * Getter
     *
     * @return Returns the facility type
     */
    public String getName() {
        return facility.getKey();
    }

    /**
     * Getter
     *
     * @return Returns the facility type
     */
    public int getIndex() {
        return facility.getValue();
    }
}
