package org.han.ica.asd.c.businessrule.parser.ast.action;

public enum FacilityType {
    FACTORY("factory", 0),
    DISTRIBUTOR("distributor", 1),
    WHOLESALER("wholesaler", 2),
    RETAILER("retailer", 3);

    private KeyValue<String, Integer> facility;

    /**
     * Constructor
     *
     * @param facilityString Name of the facility
     * @param facilityInt Number that corresponds to the facility
     */
    FacilityType(String facilityString, int facilityInt) {
        facility = new KeyValue<>();
        facility.put(facilityString,facilityInt);
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
