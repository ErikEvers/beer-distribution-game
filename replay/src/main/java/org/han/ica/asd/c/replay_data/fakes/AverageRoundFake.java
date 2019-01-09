package org.han.ica.asd.c.replay_data.fakes;

import org.han.ica.asd.c.gamevalue.GameValue;

public class AverageRoundFake {
    private int roundId;
    private GameValue facilityType;
    private double budget;
    private double stock;
    private double backorders;
    private double orderAmount;
    private double deliverAmount;

    public AverageRoundFake(int roundId, GameValue facilityType, double budget, double stock, double backorders, double orderAmount, double deliverAmount) {
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

    public void setFacilityType(GameValue facilityType) {
        this.facilityType = facilityType;
    }

    public double getAttribute(GameValue wantedValue) {
        switch (wantedValue) {
            case BUDGET:
                return budget*roundId;
            case STOCK:
                return stock*roundId;
            case BACKLOG:
                return backorders*roundId;
            case ORDERED:
                return orderAmount*roundId;
            case OUTGOINGGOODS:
                return deliverAmount*roundId;
            default:
                return 0;
        }
    }
}
