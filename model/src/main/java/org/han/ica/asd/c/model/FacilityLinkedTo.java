package org.han.ica.asd.c.model;

public class FacilityLinkedTo {
    private String gameId;
    private Facility facilityOrder;
    private Facility facilityDeliver;
    private boolean active;

    public FacilityLinkedTo(String gameId, Facility facilityOrder, Facility facilityDeliver, boolean active) {
        this.gameId = gameId;
        this.facilityOrder = facilityOrder;
        this.facilityDeliver = facilityDeliver;
        this.active = active;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Facility getFacilityOrder() {
        return facilityOrder;
    }

    public void setFacilityOrder(Facility facilityOrder) {
        this.facilityOrder = facilityOrder;
    }

    public Facility getFacilityDeliver() {
        return facilityDeliver;
    }

    public void setFacilityDeliver(Facility facilityDeliver) {
        this.facilityDeliver = facilityDeliver;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
