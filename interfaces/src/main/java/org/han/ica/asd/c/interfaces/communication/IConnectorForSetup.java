package org.han.ica.asd.c.interfaces.communication;


import org.han.ica.asd.c.exceptions.communication.DiscoveryException;
import org.han.ica.asd.c.exceptions.communication.RoomException;
import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IConnectorForSetup {
    void start();
    List<String> getAvailableRooms();
    RoomModel createRoom(String roomName, String password, BeerGame beerGame);
    RoomModel joinRoom(String roomName, String password) throws RoomException, DiscoveryException;
    RoomModel updateRoom(RoomModel room);
    void removeHostFromRoom(RoomModel room, String hostIP);
    void removeYourselfFromRoom(RoomModel room);
    void addObserver(IConnectorObserver connectorObserver);
    Map<String, String> listAllIPs();

    void chooseFacility(Facility facility, String playerId) throws FacilityNotAvailableException, SendGameMessageException;
    GamePlayerId getGameData(String userName) throws SendGameMessageException;
}
