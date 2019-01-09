package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.List;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.REQUEST_ALL_FACILITIES_MESSAGE;

public class RequestAllFacilitiesMessage extends GameMessage{
    List<Facility> facilities;

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
