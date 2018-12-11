package org.han.ica.asd.c.model;

public class FacilityTurn {
    private int roundId;
    private int facilityIdOrder;
    private int facilityIdDeliver;
    private String gameName;
    private String gameDate;
    private String gameEndDate;
    private int stock;
    private int remainingBudget;
    private Order order;
    private OpenOrder openOrder;
    private OutgoingGoods outgoingGoods;

    public FacilityTurn(int roundID, int facilityIdOrder, int facilityIdDeliver, String gameName, String gameDate, String gameEndDate, int stock, int remainingBudget, Order order, OpenOrder openOrder, OutgoingGoods outgoingGoods) {
        this.roundId = roundID;
        this.facilityIdOrder = facilityIdOrder;
        this.facilityIdDeliver = facilityIdDeliver;
        this.gameName = gameName;
        this.gameDate = gameDate;
        this.gameEndDate = gameEndDate;
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
