package org.han.ica.asd.c.discovery;

import org.han.ica.asd.c.model.domain_objects.RoomModel;

import java.util.List;

public interface IFinder {
    List<String> getAvailableRooms() throws DiscoveryException;
    RoomModel createGameRoomModel(String roomName, String leaderIP, String password) throws DiscoveryException;
    RoomModel joinGameRoomModel(String roomName, String hostIP, String password) throws DiscoveryException;
    void startGameRoom(String roomName) throws DiscoveryException;
    RoomModel getRoom(RoomModel roomModel);
}
