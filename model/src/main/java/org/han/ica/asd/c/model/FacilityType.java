package org.han.ica.asd.c.model;

public class FacilityType {
    private String facilityType;
    private String gameName;
    private String gameDate;
    private String gameEndDate;
    private int valueIncomingGoods;
    private int valueOutgoingGoods;
    private int stockHoldingCosts;
    private int openOrderCosts;
    private int startingBudget;
    private int startingOrder;

    public FacilityType(String facilityType, String gameName, String gameDate, String gameEndDate, int valueIncomingGoods, int valueOutgoingGoods, int stockHoldingCosts, int openOrderCosts, int startingBudget, int startingOrder) {
        this.facilityType = facilityType;
        this.gameName = gameName;
        this.gameDate = gameDate;
        this.gameEndDate = gameEndDate;
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
