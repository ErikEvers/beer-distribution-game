package org.han.ica.asd.c.model.domain_objects;

import org.han.ica.asd.c.gamevalue.GameValue;

public class AverageRound implements IDomainModel {
    private int roundId;
    private GameValue facilityType;
    private double budget;
    private double stock;
    private double backorders;
    private double orderAmount;
    private double deliverAmount;

    public AverageRound(int roundId, GameValue facilityType, double budget, double stock, double backorders, double orderAmount, double deliverAmount) {
        this.roundId = roundId;
        this.facilityType = facilityType;
        this.budget = budget;
        this.stock = stock;
        this.backorders = backorders;
        this.orderAmount = orderAmount;
        this.deliverAmount = deliverAmount;
    }

    public int getRoundId() {
        return roundId;
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
