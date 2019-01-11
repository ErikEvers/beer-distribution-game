package org.han.ica.asd.c.interfaces.communication;

import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;

import java.util.List;

public interface IFacilityMessageObserver extends IConnectorObserver {
    void chooseFacility(Facility facility, String playerId) throws FacilityNotAvailableException;
    GamePlayerId getGameData(String playerIp);

}
