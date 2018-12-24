package org.han.ica.asd.c.model.domain_objects;

import java.util.List;

public class RoomModel {
    private String leaderIP;
    private List<String> hosts;
    private String roomID;
    private String roomName;
    private String password;
    private Boolean gameStarted;

    public String getLeaderIP() {
        return leaderIP;
    }

    public void setLeaderIP(String leaderIP) {
        this.leaderIP = leaderIP;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(Boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
}
