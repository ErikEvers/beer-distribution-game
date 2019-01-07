package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Facility;

public class git config user.name extends GameMessage{
    private Facility facility;

    public FacilityMessage(Facility facility) {
        super(5);
        this.facility = facility;
    }

    public Facility getFacility() {
        return facility;
    }
}
