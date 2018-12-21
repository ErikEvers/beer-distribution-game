package org.han.ica.asd.c;

import org.han.ica.asd.c.discovery.DiscoveryException;
import org.han.ica.asd.c.discovery.RoomModel;

import java.util.List;

public interface IConnecterForSetup {
    List<String> getAvailableRooms() throws DiscoveryException;
    RoomModel createRoom(String roomName, String ip, String password);
    RoomModel joinRoom(String roomName, String ip, String password);
    RoomModel updateRoom(RoomModel room);
    void startRoom(RoomModel room);
}
