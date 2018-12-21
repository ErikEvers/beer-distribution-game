package org.han.ica.asd.c.model.domain_objects;

import java.util.List;

public class Facility implements IDomainModel{
    private FacilityType facilityType;
    private Player player;
    private GameAgent agent;
    private int facilityId;

    public Facility() {
        // empty for Guice
    }

    public Facility(FacilityType facilityType, Player player, GameAgent agent, int facilityId) {
        this.facilityType = facilityType;
        this.player = player;
        this.agent = agent;
        this.facilityId = facilityId;
    }

    public Facility(FacilityType facilityType, Player player, int facilityId) {
        this.facilityType = facilityType;
        this.player = player;
        this.facilityId = facilityId;
    }

    /**
     * Bare FacilityDB for when no player or agent is yet available
     */
    public Facility(FacilityType facilityType, int facilityId) {
        this.facilityType = facilityType;
        this.facilityId = facilityId;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
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
