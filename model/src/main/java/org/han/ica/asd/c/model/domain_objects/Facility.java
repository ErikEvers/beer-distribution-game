package org.han.ica.asd.c.model.domain_objects;

public class Facility implements IDomainModel{
    private FacilityType facilityType;
    private int facilityId;

    public Facility() {
        // empty for Guice
    }

    public Facility(FacilityType facilityType, int facilityId) {
        this.facilityType = facilityType;
        this.facilityId = facilityId;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    @Override
    public String toString() {
        return facilityType.getFacilityName();
    }
}
