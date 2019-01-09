package org.han.ica.asd.c.interfaces.gui_join_game;



import org.han.ica.asd.c.exceptions.communication.DiscoveryException;
import org.han.ica.asd.c.exceptions.communication.RoomException;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import java.util.List;

public interface IConnecterForSetup {
    List<String> getAvailableRooms();
    RoomModel createRoom(String roomName, String ip, String password);
    RoomModel joinRoom(String roomName, String ip, String password) throws RoomException, DiscoveryException;
    RoomModel updateRoom(RoomModel room);
    void startRoom(RoomModel room);
}
