package org.han.ica.asd.c.discovery;

import org.han.ica.asd.c.discovery.impl.GoogleDrive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomFinder implements IFinder{
    private static IResourceManager service = new GoogleDrive("/credentials.json");
    private ArrayList<String> rooms;
    private static final Logger LOGGER = Logger.getLogger(RoomFinder.class.getName());

    public RoomFinder() {
        rooms = new ArrayList<>();
    }

    public List<String> getAvailableRooms() throws DiscoveryException {
        try {
            updateAvailableGameRooms();
        } catch (DiscoveryException e) {
            LOGGER.log(Level.SEVERE, "No internet connection");
            throw new DiscoveryException(e);
        }
        return rooms;
    }

    public Room joinGameRoom(String roomName, String hostIP, String password) throws DiscoveryException {
        if (checkIfRoomDoesNotExists(roomName)) {
            try {
                Room room = new Room(roomName, service);
                room.addHost(hostIP, password);
                return room;
            } catch (RoomException e) {
                LOGGER.log(Level.SEVERE, "Something went wrong with the connection");
                throw new DiscoveryException(e);
            }
        }
        return null;
    }

    public Room createGameRoom(String roomName, String ip, String password) throws DiscoveryException {
        if (checkIfRoomDoesNotExists(roomName)) {
            throw new DiscoveryException("Room already exists.");
        }else {
            try {
                return new Room(roomName, ip, password, service);
            } catch (RoomException e) {
                throw new DiscoveryException(e);
            }
        }
    }

    private boolean checkIfRoomDoesNotExists(String roomName) throws DiscoveryException {
        try {
            return !service.checkIfFolderNotExists(roomName);
        } catch (IOException e) {
            throw new DiscoveryException(e);
        }
    }

    private void updateAvailableGameRooms() throws DiscoveryException {
        try {
            Map<String, String> availableRooms = service.getAllFolders();
            ArrayList<String> updatedHosts = new ArrayList<>();
            for (Map.Entry room : availableRooms.entrySet()) {
                updatedHosts.add(room.getValue().toString());
            }
            rooms = updatedHosts;
        } catch (IOException e) {
            throw new DiscoveryException(e);
        }
    }

    public static void setService(IResourceManager service) {
        RoomFinder.service = service;
    }
}
