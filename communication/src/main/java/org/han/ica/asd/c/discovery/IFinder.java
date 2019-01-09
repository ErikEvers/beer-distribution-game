package org.han.ica.asd.c.discovery;

import org.han.ica.asd.c.exceptions.communication.DiscoveryException;
import org.han.ica.asd.c.exceptions.communication.RoomException;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import java.util.List;

public interface IFinder {
    List<String> getAvailableRooms() throws DiscoveryException;
    RoomModel createGameRoomModel(String roomName, String leaderIP, String password) throws DiscoveryException;
    RoomModel joinGameRoomModel(String roomName, String hostIP, String password) throws DiscoveryException, RoomException;
    void startGameRoom(String roomName) throws DiscoveryException;
    RoomModel getRoom(RoomModel roomModel) throws DiscoveryException;
    void removeHostFromRoom(RoomModel roomModel, String hostIP) throws DiscoveryException;
}
