package org.han.ica.asd.c.model;

public class OpenOrder {
    private String gameId;
    private int facilityIdOrder;
    private int roundId;
    private int facilityIdDeliver;
    private int openOrderAmount;

    public OpenOrder(String gameId, int facilityIdOrder, int roundId, int facilityIdDeliver, int openOrderAmount) {
        this.gameId = gameId;
        this.facilityIdOrder = facilityIdOrder;
        this.roundId = roundId;
        this.facilityIdDeliver = facilityIdDeliver;
        this.openOrderAmount = openOrderAmount;
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

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getFacilityIdDeliver() {
        return facilityIdDeliver;
    }

    public void setFacilityIdDeliver(int facilityIdDeliver) {
        this.facilityIdDeliver = facilityIdDeliver;
    }

    public int getOpenOrderAmount() {
        return openOrderAmount;
    }

    public void setOpenOrderAmount(int openOrderAmount) {
        this.openOrderAmount = openOrderAmount;
    }
}
