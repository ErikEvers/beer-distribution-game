package org.han.ica.asd.c.interfaces.communication;

import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;

import java.util.List;

public interface IFacilityMessageObserver extends IConnectorObserver {
    void chooseFacility(Facility facility) throws Exception;
    GamePlayerId getGameData(String playerIp);

}
