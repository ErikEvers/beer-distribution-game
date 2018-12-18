package org.han.ica.asd.c.model;

import java.util.List;

public class Facility {

    private FacilityType facilityType;
    private List<FacilityLinkedTo> facilitiesLinkedTo;
    private Player player;
    private GameAgent agent;
    private int facilityId;

    public Facility(FacilityType facilityType, List<FacilityLinkedTo> facilitiesLinkedTo, Player player, GameAgent agent, int facilityId) {
        this.facilityType = facilityType;
        this.facilitiesLinkedTo = facilitiesLinkedTo;
        this.player = player;
        this.agent = agent;
        this.facilityId = facilityId;
    }

    public Facility(FacilityType facilityType, List<FacilityLinkedTo> facilitiesLinkedTo, Player player, int facilityId) {
        this.facilityType = facilityType;
        this.facilitiesLinkedTo = facilitiesLinkedTo;
        this.player = player;
        this.facilityId = facilityId;
    }

    /**
     * Bare Facility for when no player or agent is yet available
     */
    public Facility(FacilityType facilityType, List<FacilityLinkedTo> facilitiesLinkedTo, int facilityId) {
        this.facilityType = facilityType;
        this.facilitiesLinkedTo = facilitiesLinkedTo;
        this.facilityId = facilityId;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    public List<FacilityLinkedTo> getFacilitiesLinkedTo() {
        return facilitiesLinkedTo;
    }

    public void setFacilitiesLinkedTo(List<FacilityLinkedTo> facilitiesLinkedTo) {
        this.facilitiesLinkedTo = facilitiesLinkedTo;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameAgent getAgent() {
        return agent;
    }

    public void setAgent(GameAgent agent) {
        this.agent = agent;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }
}
