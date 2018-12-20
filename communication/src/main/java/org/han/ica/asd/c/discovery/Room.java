package org.han.ica.asd.c.discovery;

import org.apache.commons.validator.routines.InetAddressValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Room {
    private IResourceManager service;
    private String leaderIP;
    private List<String> hosts;
    private String roomID;
    private String roomName;
    private String password;
    private Boolean gameStarted;
    private InetAddressValidator validatorIP = InetAddressValidator.getInstance();
    private String connectionError = "Something went wrong with the connection";

    private static final Logger LOGGER = Logger.getLogger(Room.class.getName());

    public Room(String roomName, IResourceManager service) throws RoomException {
        try {
            this.service = service;
            this.roomName = roomName;
            this.roomID = service.getFolderID(roomName);
            this.leaderIP = service.getLeaderFromFolder(roomID);
            this.hosts = service.getAllhostsFromFolder(roomID);
            this.password = service.getPasswordFromFolder(roomID);
            this.gameStarted = false;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not get room.");
            throw new RoomException("Could not get room.");
        }
    }

    public Room(String roomName, String ip, String password, IResourceManager service) throws RoomException {
        if (isValidIPv4(ip)) {
            this.roomName = roomName;
            this.service = service;
            this.gameStarted = false;
            try {
                this.roomID = service.createFolder(roomName);
                service.createFileInFolder("P: " + password, roomID);
                this.password = password;
                service.createFileInFolder("L: " + ip, roomID);
                this.leaderIP = ip;
                hosts = new ArrayList<>();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, connectionError);
                throw new RoomException(e);
            }
            hosts = new ArrayList<>();
        } else {
            LOGGER.log(Level.SEVERE, "IP address is not valid.");
            throw new RoomException("IP address is not valid.");
        }
    }

    public void addHost(String ip, String password) throws RoomException {
        if ((this.password.equals(password) || this.password.isEmpty()) && !hosts.contains(ip) && !gameStarted) {
            if (isValidIPv4(ip)) {
                try {
                    service.createFileInFolder("H: " + ip, roomID);
                    hosts.add(ip);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, connectionError);
                    throw new RoomException(e);
                }
            } else {
                LOGGER.log(Level.SEVERE, "Game is started or the IP is not valid.");
                throw new RoomException("Game is started or the IP is not valid.");
            }
        } else {
            LOGGER.log(Level.SEVERE, "Password doesnt match.");
            throw new RoomException("Password doesnt match.");
        }
    }

    public void removeHost(String ip) {
        try {
            service.deleteFileByNameInFolder("H: " + ip, roomID);
            hosts.remove(ip);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, connectionError);
        }
    }

    public void updateHosts() throws RoomException {
        if (!gameStarted) {
            try {
                this.hosts = service.getAllhostsFromFolder(roomID);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, connectionError);
                throw new RoomException(e);
            }
        }
    }

    public void closeGameAndStartGame() throws RoomException {
        if (!gameStarted) {
            try {
                gameStarted = true;
                service.deleteFolderByID(roomID);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, connectionError);
                throw new RoomException(e);
            }
        }
    }

    public boolean isGameClosedForDiscovery() {
        try {
            String value = "";
            value = service.getFolderID(roomName);
            if (value.equals("")) {
                gameStarted = true;
            }
            return value.equals("");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return false;
    }

    private boolean isValidIPv4(String ip) {
        return validatorIP.isValidInet4Address(ip);
    }

    public String getLeaderIP() {
        return leaderIP;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getGameStarted() {
        return gameStarted;
    }
}
