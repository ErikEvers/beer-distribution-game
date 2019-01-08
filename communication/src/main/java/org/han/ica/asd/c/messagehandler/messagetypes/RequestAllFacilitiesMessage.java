package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.List;

public class RequestAllFacilitiesMessage extends GameMessage{
    List<Facility> facilities;
    private static final int REQUEST_ALL_FACILITIES_MESSAGE = 6;

    public RequestAllFacilitiesMessage() {
        super(REQUEST_ALL_FACILITIES_MESSAGE);
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }
}
