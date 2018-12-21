package org.han.ica.asd.c.interfaces.gui_join_game;

import org.han.ica.asd.c.model.interface_models.RoomModel;

import java.util.List;

public interface IConnectorForSetup {
    List<String> getAvailableRooms();
    RoomModel createRoom(String roomName,String password);
    RoomModel joinRoom(String roomName,String password);
    RoomModel updateRoom(String roomName);
}
