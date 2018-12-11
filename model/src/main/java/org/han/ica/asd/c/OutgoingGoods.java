package org.han.ica.asd.c;

public class OutgoingGoods {
    private String gameId;
    private int facilityIdOrder;
    private int roundId;
    private int facilityIdDeliver;
    private int outgoingGoodsAmount;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getFacilityIdOrder() {
        return facilityIdOrder;
    }

    public void setFacilityIdOrder(int facilityIdOrder) {
        this.facilityIdOrder = facilityIdOrder;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getFacilityIdDeliver() {
        return facilityIdDeliver;
    }

    public void setFacilityIdDeliver(int facilityIdDeliver) {
        this.facilityIdDeliver = facilityIdDeliver;
    }

    public int getOutgoingGoodsAmount() {
        return outgoingGoodsAmount;
    }

    public void setOutgoingGoodsAmount(int outgoingGoodsAmount) {
        this.outgoingGoodsAmount = outgoingGoodsAmount;
    }
}
