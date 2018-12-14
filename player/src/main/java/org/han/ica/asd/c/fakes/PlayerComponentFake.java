package org.han.ica.asd.c.fakes;

import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.FacilityLinkedTo;

public class PlayerComponentFake {
    //TODO: Implement actual playercomponent
    //Stub

    public FacilityLinkedTo[] seeOtherFacilities() {
        //Fake method for testing purposes
        FacilityLinkedTo[] array;

        Facility factory = new Facility("", 1, "Factory", "", "");
        Facility regionalWarehouse1 = new Facility("", 1, "Regional warehouse", "", "");
        Facility regionalWarehouse2 = new Facility("", 1, "Regional warehouse", "", "");
        Facility wholesale1 = new Facility("", 1, "Wholesale", "", "");
        Facility wholesale2 = new Facility("", 1, "Wholesale", "", "");
        Facility retailer = new Facility("", 1, "Retailer", "", "");

        FacilityLinkedTo link1 = new FacilityLinkedTo("", regionalWarehouse1, factory, true);
        FacilityLinkedTo link2 = new FacilityLinkedTo("", regionalWarehouse2, factory, false);
        FacilityLinkedTo link3 = new FacilityLinkedTo("", wholesale1, regionalWarehouse1, true);
        FacilityLinkedTo link4 = new FacilityLinkedTo("", wholesale2, regionalWarehouse2, false);
        FacilityLinkedTo link5 = new FacilityLinkedTo("", retailer, wholesale1, true);
        FacilityLinkedTo link6 = new FacilityLinkedTo("", retailer, wholesale2, false);

        array = new FacilityLinkedTo[]{link1, link2, link3, link4, link5, link6};

        return array;
    }

    public String requestFacilityInfo(Facility facility) {
        return "placeholderfac overview turn x\nBacklog: 25\nInventory: 0\nMoney: 500";
    }
}
