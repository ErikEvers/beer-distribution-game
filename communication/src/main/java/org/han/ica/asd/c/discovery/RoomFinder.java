package org.han.ica.asd.c.discovery;

import org.han.ica.asd.c.discovery.impl.GoogleDrive;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

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

    public RoomModel createGameRoomModel(String roomName, String leaderIP, String password){
        RoomModel roomModel = new RoomModel();
        try {
            Room created = createGameRoomOnline(roomName, leaderIP, password);
            roomModel.setRoomName(roomName);
            roomModel.setLeaderIP(leaderIP);
            roomModel.setHosts(new ArrayList<String>());
            roomModel.setPassword(password);
            roomModel.setGameStarted(false);
            return roomModel;
        } catch (DiscoveryException e) {
            e.printStackTrace();
        }
        return roomModel;
    }

    public RoomModel joinGameRoomModel(String roomName, String hostIP, String password){
        RoomModel roomModel = new RoomModel();
        try {
            Room created = getRoom(roomName);
            created.addHost(hostIP, password);
            roomModel.setRoomName(roomName);
            roomModel.setLeaderIP(created.getLeaderIP());
            roomModel.setHosts(created.getHosts());
            roomModel.setPassword(password);
            roomModel.setGameStarted(false);
            return roomModel;
        } catch (DiscoveryException e) {
            e.printStackTrace();
        } catch (RoomException e) {
            e.printStackTrace();
        }
        return new RoomModel();
    }

    public void startGameRoom(String roomName){
        try {
            getRoom(roomName).closeGameAndStartGame();
        } catch (RoomException e) {
            e.printStackTrace();
        } catch (DiscoveryException e) {
            e.printStackTrace();
        }
    }

    public RoomModel getRoom(RoomModel roomModel){
        RoomModel room = new RoomModel();
        try {
            Room onlineRoom = getRoom(roomModel.getRoomName());
            room.setRoomName(onlineRoom.getRoomName());
            room.setHosts(onlineRoom.getHosts());
            room.setLeaderIP(onlineRoom.getLeaderIP());
            room.setPassword(onlineRoom.getPassword());
            room.setGameStarted(false);
        } catch (DiscoveryException e) {
            e.printStackTrace();
        }
        return room;
    }

    private Room getRoom(String roomName) throws DiscoveryException {
        Room room = null;
        try {
            room = new Room(roomName, service);
        } catch (RoomException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong with the connection");
                throw new DiscoveryException(e);
        }
        return room;
    }

    private Room createGameRoomOnline(String roomName, String ip, String password) throws DiscoveryException {
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
