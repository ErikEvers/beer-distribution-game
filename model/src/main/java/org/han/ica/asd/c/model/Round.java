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


    public void addTurnOrder(Facility facilityFrom, Facility facilityTo, Integer OrderAmount) {
        Map<Facility, Integer> orderTo = new HashMap();
        orderTo.put(facilityTo, OrderAmount);
        turnOrder.put(facilityFrom, orderTo);
    }

    public int getTurnOrderByFacility(Facility facilityFrom, Facility facilityTo) {
        return turnOrder.get(facilityFrom).get(facilityTo);
    }

    public void addTurnDeliver(Facility facilityFrom, Facility facilityTo, Integer OrderAmount) {
        Map<Facility, Integer> orderTo = new HashMap();
        orderTo.put(facilityTo, OrderAmount);
        turnDeliver.put(facilityFrom, orderTo);
    }

    public int getTurnDeliverByFacility(Facility facilityFrom, Facility facilityTo) {
        return turnDeliver.get(facilityFrom).get(facilityTo);
    }

    public void addTurnReceived(Map<Facility, Integer> order, Facility facilityTo) {
        turnReceived.put(facilityTo, order);
    }

    public void addTurnBackOrder(Map<Facility, Integer> order, Facility facilityFrom) {
        turnBackOrder.put(facilityFrom, order);
    }

    public void addFacilityStock(Integer stockNumber, Facility facility) {
        stock.put(facility, stockNumber);
    }

    public int getStockByFacility(Facility facility) {
        return stock.get(facility);
    }

    public void replaceStock(Facility facility, Integer oldValue, Integer newStock) {
        stock.replace(facility, newStock);
    }

    public void addFacilityremainingStock(Integer remainingBudgetNumber, Facility facility) {
        remainingBudget.put(facility, remainingBudgetNumber);
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
