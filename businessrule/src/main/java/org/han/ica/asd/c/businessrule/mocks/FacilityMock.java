package org.han.ica.asd.c.businessrule.mocks;
//used in business rule substituteBusinessRuleWithData
public class FacilityMock {

    public int getFacilityId() {
        return facilityId;
    }

    private int facilityId;

    public FacilityMock(int facilityId){
        this.facilityId = facilityId;
    }
}
