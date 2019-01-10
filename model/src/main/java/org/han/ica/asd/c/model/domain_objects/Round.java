package org.han.ica.asd.c.model.domain_objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Round implements IDomainModel{
    private int roundId;
    private List<FacilityTurn> facilityTurns;
    private List<FacilityTurnOrder> facilityOrders;
    private List<FacilityTurnDeliver>facilityTurnDelivers;

    public Round () {
       facilityTurns = new ArrayList<>();
       facilityOrders = new ArrayList<>();
       facilityTurnDelivers = new ArrayList<>();
    }

    public Round(int roundId, List<FacilityTurn> facilityTurns, List<FacilityTurnOrder> facilityOrders,
                 List<FacilityTurnDeliver> facilityTurnDelivers) {
        this.roundId = roundId;
        this.facilityTurns = facilityTurns;
        this.facilityOrders = facilityOrders;
        this.facilityTurnDelivers = facilityTurnDelivers;
    }

    //Order
    public void addTurnOrder(Facility facilityFrom, Facility facilityTo, int orderAmount) {
        facilityOrders.add(new FacilityTurnOrder(facilityFrom.getFacilityId(), facilityTo.getFacilityId(),
                orderAmount));
    }

    public int getTurnOrderByFacility(Facility facilityFrom, Facility facilityTo) {
        for(FacilityTurnOrder o : facilityOrders) {
            if(o.getFacilityId() == facilityFrom.getFacilityId() && o.getFacilityIdOrderTo() == facilityTo.getFacilityId()) {
                return o.getOrderAmount();
            }
        }
        return -1; //TODO
    }

    //Deliver
    public void addTurnDeliver(Facility facilityFrom, Facility facilityTo, int orderAmount, int openOrderAmount) {
        facilityTurnDelivers.add(new FacilityTurnDeliver(facilityFrom.getFacilityId(), facilityTo.getFacilityId(),
                openOrderAmount, orderAmount));
    }

    public int getTurnDeliverByFacility(Facility facilityFrom, Facility facilityTo) {
        for(FacilityTurnDeliver o : facilityTurnDelivers) {
            if(o.getFacilityId() == facilityFrom.getFacilityId() && o.getFacilityIdDeliverTo() == facilityTo.getFacilityId()) {
                return o.getDeliverAmount();
            }
        }

        return -1; //TODO
    }

//    //Backlog

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

    public List<FacilityTurn> getFacilityTurns() {
        return facilityTurns;
    }

    public void setFacilityTurns(List<FacilityTurn> facilityTurns) {
        this.facilityTurns = facilityTurns;
    }

    public List<FacilityTurnOrder> getFacilityOrders() {
        return facilityOrders;
    }

    public void setFacilityOrders(List<FacilityTurnOrder> facilityOrders) {
        this.facilityOrders = facilityOrders;
    }

    public List<FacilityTurnDeliver> getFacilityTurnDelivers() {
        return facilityTurnDelivers;
    }

    public void setFacilityTurnDelivers(List<FacilityTurnDeliver> facilityTurnDelivers) {
        this.facilityTurnDelivers = facilityTurnDelivers;
    }
}
