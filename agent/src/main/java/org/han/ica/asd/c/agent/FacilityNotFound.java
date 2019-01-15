package org.han.ica.asd.c.agent;

class FacilityNotFound extends Throwable {
    private static final String FACILITY_NOT_FOUND_MESSAGE = "Facility with ID: %s is not found.";

    FacilityNotFound(int facilityId) {
        super(String.format(FACILITY_NOT_FOUND_MESSAGE, facilityId));
    }
}
