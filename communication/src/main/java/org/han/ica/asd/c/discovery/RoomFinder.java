package org.han.ica.asd.c.discovery;

import org.han.ica.asd.c.discovery.impl.GoogleDrive;
import org.han.ica.asd.c.exceptions.communication.DiscoveryException;
import org.han.ica.asd.c.exceptions.communication.RoomException;
import org.han.ica.asd.c.interfaces.communication.IFinder;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomFinder implements IFinder {
    private static IResourceManager service = new GoogleDrive("/credentials.json");
    private ArrayList<String> rooms;

    public RoomFinder() {
        rooms = new ArrayList<>();
    }

    public List<String> getAvailableRooms() throws DiscoveryException {
        try {
            updateAvailableGameRooms();
        } catch (DiscoveryException e) {
            throw new DiscoveryException("No internet connection");
        }
        return rooms;
    }

    public RoomModel createGameRoomModel(String roomName, String leaderIP, String password) throws DiscoveryException{
        RoomModel roomModel = new RoomModel();
        try {
            createGameRoomOnline(roomName, leaderIP, password);
            roomModel.setRoomName(roomName);
            roomModel.setLeaderIP(leaderIP);
            roomModel.setHosts(new ArrayList<String>());
            roomModel.setPassword(password);
            roomModel.setGameStarted(false);
            return roomModel;
        } catch (DiscoveryException e) {
            throw new DiscoveryException(e.getMessage());
        }
    }

    public RoomModel joinGameRoomModel(String roomName, String hostIP, String password) throws DiscoveryException, RoomException {
        RoomModel roomModel = new RoomModel();
        Room created = getRoom(roomName);
        created.addHost(hostIP, password);
        roomModel.setRoomName(roomName);
        roomModel.setLeaderIP(created.getLeaderIP());
        roomModel.setHosts(created.getHosts());
        roomModel.setPassword(password);
        roomModel.setGameStarted(false);
        return roomModel;
    }

    public void startGameRoom(String roomName) throws DiscoveryException {
        try {
            getRoom(roomName).closeGameAndStartGame();
        } catch (RoomException | DiscoveryException e) {
            throw new DiscoveryException(e);
        }
    }

    public RoomModel getRoom(RoomModel roomModel) throws DiscoveryException {
        RoomModel room = new RoomModel();
        try {
            Room onlineRoom = getRoom(roomModel.getRoomName());
            room.setRoomName(onlineRoom.getRoomName());
            room.setHosts(onlineRoom.getHosts());
            room.setLeaderIP(onlineRoom.getLeaderIP());
            room.setPassword(onlineRoom.getPassword());
            room.setGameStarted(false);
        } catch (DiscoveryException e) {
            throw new DiscoveryException(e);
        }
        return room;
    }

    @Override
    public void removeHostFromRoom(RoomModel roomModel, String hostIP) throws DiscoveryException {
        try {
            getRoom(roomModel.getRoomName()).removeHost(hostIP);
        } catch (RoomException e) {
            throw new DiscoveryException(e);
        }
    }

    private Room getRoom(String roomName) throws DiscoveryException {
        try {
            return new Room(roomName, service);
        } catch (RoomException e) {
            throw new DiscoveryException("Something went wrong with the connection");
        }
    }

    private Room createGameRoomOnline(String roomName, String ip, String password) throws DiscoveryException {
        if (checkIfRoomDoesNotExists(roomName)) {
            throw new DiscoveryException("Room already exists.");
        } else {
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

    static void setService(IResourceManager service) {
        RoomFinder.service = service;
    }
}
