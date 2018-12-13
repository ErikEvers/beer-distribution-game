package org.han.ica.asd.c.fakes;

import org.han.ica.asd.c.model.Facility;

public class PlayerComponentFake {
    //Stub

    public Facility[] seeOtherFacilities() {
        //Fake method for testing purposes
        Facility[] facilities;

        Facility factory = new Facility("", 1, "Factory", "", "");
        Facility regionalWarehouse = new Facility("", 1, "Regional warehouse", "", "");
        Facility wholesale = new Facility("", 1, "Wholesale", "", "");
        Facility retailer = new Facility("", 1, "Retailer", "", "");

        facilities = new Facility[]{factory, regionalWarehouse, wholesale, retailer};

        return facilities;
    }
}
