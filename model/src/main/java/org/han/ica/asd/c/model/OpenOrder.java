package org.han.ica.asd.c.model;

public class OpenOrder {
    private String gameName;
    private String gameDate;
    private String gameEndDate;
    private int facilityIdOrder;
    private int roundId;
    private int facilityIdDeliver;
    private int openOrderAmount;

    public OpenOrder(String gameName, String gameDate, String gameEndDate, int facilityIdOrder, int roundId, int facilityIdDeliver, int openOrderAmount) {
        this.gameName = gameName;
        this.gameDate = gameDate;
        this.gameEndDate = gameEndDate;
        this.facilityIdOrder = facilityIdOrder;
        this.roundId = roundId;
        this.facilityIdDeliver = facilityIdDeliver;
        this.openOrderAmount = openOrderAmount;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getGameEndDate() {
        return gameEndDate;
    }

    public void setGameEndDate(String gameEndDate) {
        this.gameEndDate = gameEndDate;
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
