package org.han.ica.asd.c.model.domain_objects;

public class FacilityLinkedTo {
    private String gameId;
    private Facility facilityIdDeliver;
    private boolean active;

    public FacilityLinkedTo(String gameId, Facility facilityIdDeliver, boolean active) {
        this.gameId = gameId;
        this.facilityIdDeliver = facilityIdDeliver;
        this.active = active;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Facility getFacilityIdDeliver() {
        return facilityIdDeliver;
    }

    public void setFacilityIdDeliver(Facility facilityIdDeliver) {
        this.facilityIdDeliver = facilityIdDeliver;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
