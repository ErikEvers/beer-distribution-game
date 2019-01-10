package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.ArrayList;
import java.util.List;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.FACILITY_MESSAGE;

public class ChooseFacilityMessage extends GameMessage{
    private Facility facility;
    private String playerId;

    public ChooseFacilityMessage(Facility facility) {
        super(FACILITY_MESSAGE);
        this.facility = facility;
    }

    public ChooseFacilityMessage(Facility facility, String playerId) {
        super(FACILITY_MESSAGE);
        this.facility = facility;
        this.playerId = playerId;
    }

    public Facility getFacility() {
        return facility;
    }

    public String getPlayerId() {
        return playerId;
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
