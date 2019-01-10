package org.han.ica.asd.c.model.domain_objects;

import java.io.Serializable;

public class FacilityType implements IDomainModel, Serializable {
    private String facilityName;
    private int valueIncomingGoods;
    private int valueOutgoingGoods;
    private int stockHoldingCosts;
    private int openOrderCosts;
    private int startingBudget;
    private int startingOrder;
    private int startingStock;

    public FacilityType(){
        //Empty constructor for Guice
    }

    public FacilityType(String facilityName, int valueIncomingGoods, int valueOutgoingGoods, int stockHoldingCosts, int openOrderCosts, int startingBudget, int startingOrder, int startingStock) {
        this.facilityName = facilityName;
        this.valueIncomingGoods = valueIncomingGoods;
        this.valueOutgoingGoods = valueOutgoingGoods;
        this.stockHoldingCosts = stockHoldingCosts;
        this.openOrderCosts = openOrderCosts;
        this.startingBudget = startingBudget;
        this.startingOrder = startingOrder;
        this.startingStock = startingStock;
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

    public int getStartingStock() {
        return startingStock;
    }

    public void setStartingStock(int startingStock) {
        this.startingStock = startingStock;
    }
}
