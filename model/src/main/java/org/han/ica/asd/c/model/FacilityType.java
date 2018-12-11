package org.han.ica.asd.c.model;

public class FacilityType {
    private String facilityType;
    private String gameId;
    private int valueIncomingGoods;
    private int valueOutgoingGoods;
    private int stockHoldingCosts;
    private int openOrderCosts;
    private int startingBudget;
    private int startingOrder;

    public FacilityType(String facilityType, String gameId, int valueIncomingGoods, int valueOutgoingGoods, int stockHoldingCosts, int openOrderCosts, int startingBudget, int startingOrder) {
        this.facilityType = facilityType;
        this.gameId = gameId;
        this.valueIncomingGoods = valueIncomingGoods;
        this.valueOutgoingGoods = valueOutgoingGoods;
        this.stockHoldingCosts = stockHoldingCosts;
        this.openOrderCosts = openOrderCosts;
        this.startingBudget = startingBudget;
        this.startingOrder = startingOrder;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getValueIncomingGoods() {
        return valueIncomingGoods;
    }

    public void setValueIncomingGoods(int valueIncomingGoods) {
        this.valueIncomingGoods = valueIncomingGoods;
    }

    public int getValueOutgoingGoods() {
        return valueOutgoingGoods;
    }

    public void setValueOutgoingGoods(int valueOutgoingGoods) {
        this.valueOutgoingGoods = valueOutgoingGoods;
    }

    public int getStockHoldingCosts() {
        return stockHoldingCosts;
    }

    public void setStockHoldingCosts(int stockHoldingCosts) {
        this.stockHoldingCosts = stockHoldingCosts;
    }

    public int getOpenOrderCosts() {
        return openOrderCosts;
    }

    public void setOpenOrderCosts(int openOrderCosts) {
        this.openOrderCosts = openOrderCosts;
    }

    public int getStartingBudget() {
        return startingBudget;
    }

    public void setStartingBudget(int startingBudget) {
        this.startingBudget = startingBudget;
    }

    public int getStartingOrder() {
        return startingOrder;
    }

    public void setStartingOrder(int startingOrder) {
        this.startingOrder = startingOrder;
    }
}
