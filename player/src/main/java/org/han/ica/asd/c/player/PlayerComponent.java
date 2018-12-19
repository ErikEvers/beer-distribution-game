package org.han.ica.asd.c.player;

import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.FacilityLinkedTo;

public class PlayerComponent {

    public FacilityLinkedTo[] seeOtherFacilities() {
        //Fake method for testing purposes
        FacilityLinkedTo[] array;

        FacilityLinkedTo link1 = new FacilityLinkedTo("", 0, 1, true);

        array = new FacilityLinkedTo[]{link1};

        return array;
    }

    public String requestFacilityInfo(Facility facility) {
        //Fake method for testing purposes
        return "placeholderfac overview turn x\nBacklog: 25\nInventory: 0\nMoney: 500";
    }
}
