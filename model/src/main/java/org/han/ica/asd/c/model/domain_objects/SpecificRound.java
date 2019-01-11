package org.han.ica.asd.c.model.domain_objects;

import org.han.ica.asd.c.gamevalue.GameValue;

public class SpecificRound implements IDomainModel {

    private int roundID;
    private GameValue facilityType;
    private double budget;
    private double stock;
    private double backorders;
    private double orderAmount;
    private double deliverAmount;

    public SpecificRound(int roundID, GameValue facilityType, double budget, double stock, double backorders, double orderAmount, double deliverAmount) {
        this.roundID = roundID;
        this.facilityType = facilityType;
        this.budget = budget;
        this.stock = stock;
        this.backorders = backorders;
        this.orderAmount = orderAmount;
        this.deliverAmount = deliverAmount;
    }

    public int getRoundID() {
        return roundID;
    }

    public GameValue getFacilityType() {
        return facilityType;
    }

    public double getBudget() {
        return budget;
    }

    public double getStock() {
        return stock;
    }

    public double getBackorders() {
        return backorders;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public double getDeliverAmount() {
        return deliverAmount;
    }
}
