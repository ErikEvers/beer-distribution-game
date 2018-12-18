package org.han.ica.asd.c.model.dao_model;

public class FacilityTurn implements IDaoModel{
    private int roundId;
    private int facilityIdOrder;
    private int facilityIdDeliver;
    private String gameId;
    private int stock;
    private int remainingBudget;
    private int order;
    private int openOrder;
    private int outgoingGoods;

    public FacilityTurn(String gameId, int facilityIdOrder, int facilityIdDeliver, int roundId, int stock, int remainingBudget, int order, int openOrder, int outgoingGoods) {
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOpenOrder() {
        return openOrder;
    }

    public void setOpenOrder(int openOrder) {
        this.openOrder = openOrder;
    }

    public int getOutgoingGoods() {
        return outgoingGoods;
    }

    public void setOutgoingGoods(int outgoingGoods) {
        this.outgoingGoods = outgoingGoods;
    }
}
