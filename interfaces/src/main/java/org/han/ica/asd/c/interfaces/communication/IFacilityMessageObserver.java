package org.han.ica.asd.c.interfaces.communication;

import org.han.ica.asd.c.model.domain_objects.Facility;

import java.util.List;

public interface IFacilityMessageObserver extends IConnectorObserver {
    List<Facility> facilityMessageReceived(Facility facility);
}
