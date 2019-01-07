package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.ArrayList;
import java.util.List;

public class FacilityMessage extends GameMessage{
    private Facility facility;
    private List<Facility> availableFacilities;
    private static final int FACILITYMESSAGE = 5;

    public FacilityMessage(Facility facility) {
        super(FACILITYMESSAGE);
        this.facility = facility;
    }

    public FacilityMessage(Facility facility, List<Facility> availableFacilities) {
        super(FACILITYMESSAGE);
        this.facility = facility;
        this.availableFacilities = availableFacilities;
    }

    public Facility getFacility() {
        return facility;
    }

    public List<Facility> getAvailableFacilities() {
        return availableFacilities;
    }
}
