package org.han.ica.asd.c.model;

public class Order {
    private String gameId;
    private int facilityIdOrder;
    private int roundId;
    private int facilityIdDeliver;
    private int orderAmount;

    public Order(String gameId, int facilityIdOrder, int roundId, int facilityIdDeliver, int orderAmount) {
        this.gameId = gameId;
        this.facilityIdOrder = facilityIdOrder;
        this.roundId = roundId;
        this.facilityIdDeliver = facilityIdDeliver;
        this.orderAmount = orderAmount;
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

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }
}
