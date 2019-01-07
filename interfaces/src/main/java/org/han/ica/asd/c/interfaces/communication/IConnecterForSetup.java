package org.han.ica.asd.c.interfaces.communication;



import org.han.ica.asd.c.model.domain_objects.RoomModel;

import java.util.List;

public interface IConnecterForSetup {
    List<String> getAvailableRooms();
    RoomModel createRoom(String roomName, String ip, String password);
    RoomModel joinRoom(String roomName, String ip, String password);
    RoomModel updateRoom(RoomModel room);
    void startRoom(RoomModel room);
}