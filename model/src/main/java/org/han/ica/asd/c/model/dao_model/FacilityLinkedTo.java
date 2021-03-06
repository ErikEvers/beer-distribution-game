package org.han.ica.asd.c.model.dao_model;

public class FacilityLinkedTo {
    private String gameId;
    private int facilityIdOrder;
    private int facilityIdDeliver;
    private boolean active;

    public FacilityLinkedTo(String gameId, int facilityIdOrder, int facilityIdDeliver, boolean active) {
        this.gameId = gameId;
        this.facilityIdOrder = facilityIdOrder;
        this.facilityIdDeliver = facilityIdDeliver;
        this.active = active;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getFacilityIdOrder() {
        return facilityIdOrder;
    }

    public void setFacilityIdOrder(int facilityIdOrder) {
        this.facilityIdOrder = facilityIdOrder;
    }

    public int getFacilityIdDeliver() {
        return facilityIdDeliver;
    }

    public void setFacilityIdDeliver(int facilityIdDeliver) {
        this.facilityIdDeliver = facilityIdDeliver;

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
