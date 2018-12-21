package org.han.ica.asd.c.player;

import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityLinkedTo;
import org.han.ica.asd.c.model.domain_objects.FacilityType;

public class PlayerComponent {

    public FacilityLinkedTo[] seeOtherFacilities() {
        //Fake method for testing purposes
        FacilityLinkedTo[] array;

        FacilityLinkedTo link1 = new FacilityLinkedTo("", new Facility(new FacilityType("Retailer", 1, 1, 1, 2, 25, 1), 0),
						new Facility(new FacilityType("Distributor", 1, 1, 1, 2, 25, 1), 1), true);

        array = new FacilityLinkedTo[]{link1};

        return array;
    }

    public String requestFacilityInfo(Facility facility) {
        //Fake method for testing purposes
        return "placeholderfac overview turn x\nBacklog: 25\nInventory: 0\nMoney: 500";
    }
}
