package org.han.ica.asd.c.GameConfiguration;

import org.han.ica.asd.c.Exceptions.FacilityNotFoundException;
import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.List;

public class FacilityHandler {

  public Facility getFacilityFromList(List<Facility> facilities, int facilityId) throws FacilityNotFoundException {
    if(!facilities.isEmpty()) {
      for (Facility facility: facilities) {
        if(facilityId == facility.getFacilityId()) {

        } else {
          throw new FacilityNotFoundException("The selected facility does not exist.");
        }
      }
    }
    throw new FacilityNotFoundException("The list with facilities is empty.");
  }
}
