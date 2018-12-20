package org.han.ica.asd.c.model.domain_objects;


import java.util.HashMap;
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

    //Order
    public void addTurnOrder(Facility facilityFrom, Facility facilityTo, Integer orderAmount) {
        Map<Facility, Integer> orderTo = new HashMap();
        orderTo.put(facilityTo, orderAmount);
        turnOrder.put(facilityFrom, orderTo);
    }

    public int getTurnOrderByFacility(Facility facilityFrom, Facility facilityTo) {
        return turnOrder.get(facilityFrom).get(facilityTo);
    }

    //Deliver
    public void addTurnDeliver(Facility facilityFrom, Facility facilityTo, Integer orderAmount) {
        Map<Facility, Integer> orderTo = new HashMap();
        orderTo.put(facilityTo, orderAmount);
        turnDeliver.put(facilityFrom, orderTo);
    }

    public int getTurnDeliverByFacility(Facility facilityFrom, Facility facilityTo) {
        return turnDeliver.get(facilityFrom).get(facilityTo);
    }

    //Received
    public void addTurnReceived(Facility facilityFrom, Facility facilityTo, Integer orderAmount) {
        if (turnReceived.containsKey(facilityTo)) {
            turnReceived.get(facilityTo).put(facilityFrom, orderAmount);
        } else {
            Map<Facility, Integer> orderTo = new HashMap();
            orderTo.put(facilityFrom, orderAmount);
            turnReceived.put(facilityTo, orderTo);
        }
    }

    public int getTurnReceivedByFacility(Facility facilityFrom, Facility facilityTo) {
        return turnReceived.get(facilityTo).get(facilityFrom);
    }

    //Backlog
    public void addTurnBackOrder(Facility facilityFrom, Facility facilityTo, Integer orderAmount) {
        Map<Facility, Integer> orderTo = new HashMap();
        orderTo.put(facilityTo, orderAmount);
        turnBackOrder.put(facilityFrom, orderTo);
    }

    public int getTurnBacklogByFacility(Facility facilityFrom, Facility facilityTo) {
        return turnBackOrder.get(facilityFrom).get(facilityTo);
    }

    public int updateTurnBacklogByFacility(Facility facilityFrom, Facility facilityTo, int newValue) {
        return turnBackOrder.get(facilityFrom).replace(facilityTo, newValue);
    }

    public boolean isTurnBackLogFilledByFacility(Facility facilityFrom) {
        return turnBackOrder.containsKey(facilityFrom);
    }

    //stock
    public void addFacilityStock(Integer stockNumber, Facility facility) {
        turnStock.put(facility, stockNumber);
    }

    public int getStockByFacility(Facility facility) {
        return turnStock.get(facility);
    }

    public void setStock(Map<Facility, Integer> stock) {
        this.turnStock = stock;
    }

    public Map<Facility, Integer> getStock() {
        return turnStock;
    }

    public boolean isStockExisting(Facility facility) {
        return turnStock.containsKey(facility);
    }

    public void updateStock(Facility facility, Integer newStock) {
        turnStock.replace(facility, newStock);
    }

    //Remaining budget
    public void addFacilityRemainingBudget(Integer remainingBudgetNumber, Facility facility) {
        remainingBudget.put(facility, remainingBudgetNumber);
    }

    public int getRemainingBudgetByFacility(Facility facility) {
        return remainingBudget.get(facility);
    }

    public void updateRemainingBudget(Integer remainingBudgetNumber, Facility facility) {
        remainingBudget.replace(facility, remainingBudgetNumber);
    }

    public boolean isRemainingBudgetExisting(Facility facility) {
        return remainingBudget.containsKey(facility);
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
