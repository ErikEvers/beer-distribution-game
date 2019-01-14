package org.han.ica.asd.c.interfaces.communication;


import org.han.ica.asd.c.exceptions.communication.DiscoveryException;
import org.han.ica.asd.c.exceptions.communication.RoomException;
import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import java.io.IOException;
import java.util.List;

public interface IConnectorForSetup {
    void start();
    List<String> getAvailableRooms();
    RoomModel createRoom(String roomName, String password, BeerGame beerGame);
    RoomModel joinRoom(String roomName, String password) throws RoomException, DiscoveryException;
    RoomModel updateRoom(RoomModel room);
    void removeHostFromRoom(RoomModel room, String hostIP);
    void removeYourselfFromRoom(RoomModel room);
    void addObserver(IConnectorObserver connectorObserver);
    void chooseFacility(Facility facility, String playerId) throws FacilityNotAvailableException;
    GamePlayerId getGameData(String userName) throws IOException, ClassNotFoundException;
}
