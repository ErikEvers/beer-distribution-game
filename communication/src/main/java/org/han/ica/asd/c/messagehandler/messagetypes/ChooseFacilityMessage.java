package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.ArrayList;
import java.util.List;

public class ChooseFacilityMessage extends GameMessage{
    private Facility facility;
    private List<Facility> availableFacilities;
    private static final int FACILITYMESSAGE = 5;

    public ChooseFacilityMessage(Facility facility) {
        super(FACILITYMESSAGE);
        this.facility = facility;
    }

    public ChooseFacilityMessage(Facility facility, List<Facility> availableFacilities) {
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

    public ChooseFacilityMessage createResponseMessage(Exception exception){
        ChooseFacilityMessage chooseFacilityMessage = new ChooseFacilityMessage(null);
        chooseFacilityMessage.setException(exception);
        return chooseFacilityMessage;
    }
}
