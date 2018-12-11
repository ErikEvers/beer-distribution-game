package org.han.ica.asd.c.model;

public class FacilityTurn {
    private int roundId;
    private int facilityIdOrder;
    private int facilityIdDeliver;
    private String gameId;
    private int stock;
    private int remainingBudget;
    private Order order;
    private OpenOrder openOrder;
    private OutgoingGoods outgoingGoods;

    public FacilityTurn(int roundId, int facilityIdOrder, int facilityIdDeliver, String gameId, int stock, int remainingBudget, Order order, OpenOrder openOrder, OutgoingGoods outgoingGoods) {
        this.roundId = roundId;
        this.facilityIdOrder = facilityIdOrder;
        this.facilityIdDeliver = facilityIdDeliver;
        this.gameId = gameId;
        this.stock = stock;
        this.remainingBudget = remainingBudget;
        this.order = order;
        this.openOrder = openOrder;
        this.outgoingGoods = outgoingGoods;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(int remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OpenOrder getOpenOrder() {
        return openOrder;
    }

    public void setOpenOrder(OpenOrder openOrder) {
        this.openOrder = openOrder;
    }

    public OutgoingGoods getOutgoingGoods() {
        return outgoingGoods;
    }

    public void setOutgoingGoods(OutgoingGoods outgoingGoods) {
        this.outgoingGoods = outgoingGoods;
    }
}
