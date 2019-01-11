package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.ArrayList;
import java.util.List;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.FACILITY_MESSAGE;

public class ChooseFacilityMessage extends GameMessage{
    private Facility facility;
    private List<Facility> availableFacilities;

    public ChooseFacilityMessage(Facility facility) {
        super(FACILITY_MESSAGE);
        this.facility = facility;
    }

    public ChooseFacilityMessage(Facility facility, List<Facility> availableFacilities) {
        super(FACILITY_MESSAGE);
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
    public ChooseFacilityMessage createResponseMessage(){
        return new ChooseFacilityMessage(null);
    }
}
