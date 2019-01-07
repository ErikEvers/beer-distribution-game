package org.han.ica.asd.c.discovery;

import java.util.List;

public interface IFinder {
    List<String> getAvailableRooms() throws DiscoveryException;
    Room createGameRoom(String roomName, String ip, String password) throws DiscoveryException;
    Room joinGameRoom(String roomName, String hostIP, String password) throws DiscoveryException;
}
