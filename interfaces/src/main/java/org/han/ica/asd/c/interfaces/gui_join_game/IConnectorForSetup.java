package org.han.ica.asd.c.interfaces.gui_join_game;

import org.han.ica.asd.c.discovery.Room;

import java.util.List;

public interface IConnectorForSetup {
    List<String> getAvailableRooms();
    Room createRoom(String roomName, String ip, String password);
    Room joinRoom(String roomName, String ip,String password);

}
