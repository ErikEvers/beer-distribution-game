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

    public Facility getFacilityIdOrder() {
        return facilityOrder;
    }

    public void setFacilityIdOrder(Facility facilityOrder) {
        this.facilityOrder = facilityOrder;
    }

    public Facility getFacilityIdDeliver() {
        return facilityDeliver;
    }

    public void setFacilityIdDeliver(Facility facilityDeliver) {
        this.facilityDeliver = facilityDeliver;

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
