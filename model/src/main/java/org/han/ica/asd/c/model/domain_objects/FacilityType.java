package org.han.ica.asd.c.model.domain_objects;

public class FacilityType {
    private String facilityName;
    private int valueIncomingGoods;
    private int valueOutgoingGoods;
    private int stockHoldingCosts;
    private int openOrderCosts;
    private int startingBudget;
    private int startingOrder;

    public FacilityType(String facilityName, int valueIncomingGoods, int valueOutgoingGoods, int stockHoldingCosts, //NOSONAR
                        int openOrderCosts, int startingBudget, int startingOrder) { //NOSONAR
        this.facilityName = facilityName;
        this.valueIncomingGoods = valueIncomingGoods;
        this.valueOutgoingGoods = valueOutgoingGoods;
        this.stockHoldingCosts = stockHoldingCosts;
        this.openOrderCosts = openOrderCosts;
        this.startingBudget = startingBudget;
        this.startingOrder = startingOrder;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
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
