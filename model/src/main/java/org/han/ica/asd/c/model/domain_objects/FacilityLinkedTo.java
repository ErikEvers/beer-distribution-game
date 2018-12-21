package org.han.ica.asd.c.model.domain_objects;

public class FacilityLinkedTo implements IDomainModel{
    private String gameId;
    private Facility facilityDeliver;
    private boolean active;

    public FacilityLinkedTo(String gameId, Facility facilityDeliver, boolean active) {
        this.gameId = gameId;
        this.facilityDeliver = facilityDeliver;
        this.active = active;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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
