package org.han.ica.asd.c.model.domain_objects;

import java.io.Serializable;

public class Player implements IDomainModel, Serializable {
    private String playerId;
    private String ipAddress;
    private Facility facility;
    private String name;
    private boolean isConnected;

    public Player(){
        //Empty constructor for Guice
    }

    public Player(String playerId, String ipAddress, Facility facility, String name, boolean isConnected) {
        this.playerId = playerId;
        this.ipAddress = ipAddress;
        this.facility = facility;
        this.name = name;
        this.isConnected = isConnected;
    }

    public String concatIpId() {
        return playerId.concat(ipAddress);
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
