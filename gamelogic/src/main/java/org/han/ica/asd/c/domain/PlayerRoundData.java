package org.han.ica.asd.c.domain;

public class PlayerRoundData {
    private String playerId;
    private int inventory;
    private int incomingGoods;
    private int outcomingOrders;
    private int backlog;
    private float budget;

    public PlayerRoundData(String playerId, int inventory, int incomingGoods, int outcomingOrders, int backlog, float budget) {
        this.playerId = playerId;
        this.inventory = inventory;
        this.incomingGoods = incomingGoods;
        this.outcomingOrders = outcomingOrders;
        this.backlog = backlog;
        this.budget = budget;
    }
}
