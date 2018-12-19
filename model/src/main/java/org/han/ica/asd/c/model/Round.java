package org.han.ica.asd.c.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Round {
    private String gameId;
    private int roundId;
    private List<FacilityTurn> turns;

    private Map<Facility, Map<Facility, Integer>> turnOrder; //daadwerkelijk wat er bestelt is van de een naar de andere
    private Map<Facility, Map<Facility, Integer>> turnDeliver; //wat ik op deze ronde kan versturen naar de andere
    private Map<Facility, Map<Facility, Integer>> turnReceived; //turnorder maar dan de van en naar omgewisseld
    private Map<Facility, Map<Facility, Integer>> turnBackOrder; //de backorders die bij turndeliver niet verstuur dkonden worden
    private Map<Facility, Integer> stock; //de stock van iedere faciliteit
    private Map<Facility, Integer> remainingBudget; //remaining budget van iedere faciliteit

    public Round(String gameId, int roundId) {
        this.gameId = gameId;
        this.roundId = roundId;
        turns = new ArrayList<>();

        turnOrder = new HashMap<>();
        turnDeliver = new HashMap<>();
        turnReceived = new HashMap<>();
        turnBackOrder = new HashMap<>();
        stock = new HashMap<>();
        remainingBudget = new HashMap<>();
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

    public boolean isTurnBackLogfilledByFacility(Facility facilityFrom) {
        return turnBackOrder.containsKey(facilityFrom);
    }

    //stock
    public void addFacilityStock(Integer stockNumber, Facility facility) {
        stock.put(facility, stockNumber);
    }

    public int getStockByFacility(Facility facility) {
        return stock.get(facility);
    }

    public void setStock(Map<Facility, Integer> stock) {
        this.stock = stock;
    }

    public Map<Facility, Integer> getStock() {
        return stock;
    }

    public boolean isStockExisting(Facility facility) {
        return stock.containsKey(facility);
    }

    public void updateStock(Facility facility, Integer newStock) {
        stock.replace(facility, newStock);
    }

    //Remaining budget
    public void addFacilityRemainingBudget(Integer remainingBudgetNumber, Facility facility) {
        remainingBudget.put(facility, remainingBudgetNumber);
    }

    public int getRemainingBudgetByFacility(Facility facility) {
        return remainingBudget.get(facility);
    }

    public void setRemainingBudget(Map<Facility, Integer> remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public Map<Facility, Integer> getRemainingBudget() {
        return remainingBudget;
    }

    public void updateRemainingBudget(Integer remainingBudgetNumber, Facility facility) {
        remainingBudget.replace(facility, remainingBudgetNumber);
    }

    public boolean isremainingBudgetExisting(Facility facility) {
        return remainingBudget.containsKey(facility);
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public List<FacilityTurn> getTurns() {
        return turns;
    }

    public void setTurns(List<FacilityTurn> turns) {
        this.turns = turns;
    }

    public void addTurn(FacilityTurn turn){
        turns.add(turn);
    }

}
