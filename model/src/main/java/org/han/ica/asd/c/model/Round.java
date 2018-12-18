package org.han.ica.asd.c.model;


import java.util.Map;

public class Round {
    private int roundId;
    private Map<Facility, Map<Facility, Integer>> turnOrder;
    private Map<Facility, Map<Facility, Integer>> turnDeliver;
    private Map<Facility, Map<Facility, Integer>> turnReceived;
    private Map<Facility, Map<Facility, Integer>> turnBackOrder;
    private Map<Facility, Integer> turnStock;
    private Map<Facility, Integer> remainingBudget;

    public Round () {
        // Round may be void of data
    }

    public Round(int roundId, Map<Facility, Map<Facility, Integer>> turnOrder, Map<Facility, Map<Facility, Integer>> turnDeliver, //NOSONAR
                 Map<Facility, Map<Facility, Integer>> turnReceived, Map<Facility, Map<Facility, Integer>> turnBackOrder, //NOSONAR
                 Map<Facility, Integer> turnStock, Map<Facility, Integer> remainingBudget) //NOSONAR
    {
        this.roundId = roundId;
        this.turnOrder = turnOrder;
        this.turnDeliver = turnDeliver;
        this.turnReceived = turnReceived;
        this.turnBackOrder = turnBackOrder;
        this.turnStock = turnStock;
        this.remainingBudget = remainingBudget;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public Map<Facility, Map<Facility, Integer>> getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(Map<Facility, Map<Facility, Integer>> turnOrder) {
        this.turnOrder = turnOrder;
    }

    public Map<Facility, Map<Facility, Integer>> getTurnDeliver() {
        return turnDeliver;
    }

    public void setTurnDeliver(Map<Facility, Map<Facility, Integer>> turnDeliver) {
        this.turnDeliver = turnDeliver;
    }

    public Map<Facility, Map<Facility, Integer>> getTurnReceived() {
        return turnReceived;
    }

    public void setTurnReceived(Map<Facility, Map<Facility, Integer>> turnReceived) {
        this.turnReceived = turnReceived;
    }

    public Map<Facility, Map<Facility, Integer>> getTurnBackOrder() {
        return turnBackOrder;
    }

    public void setTurnBackOrder(Map<Facility, Map<Facility, Integer>> turnBackOrder) {
        this.turnBackOrder = turnBackOrder;
    }

    public Map<Facility, Integer> getTurnStock() {
        return turnStock;
    }

    public void setTurnStock(Map<Facility, Integer> turnStock) {
        this.turnStock = turnStock;
    }

    public Map<Facility, Integer> getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(Map<Facility, Integer> remainingBudget) {
        this.remainingBudget = remainingBudget;
    }
}
