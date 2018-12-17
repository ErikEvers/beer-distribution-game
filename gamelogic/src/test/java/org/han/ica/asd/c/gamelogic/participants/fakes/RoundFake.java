package org.han.ica.asd.c.gamelogic.participants.fakes;

import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.Round;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoundFake extends Round {
   /* private Map<Facility, Map<Facility, Integer>> turnOrder; //daadwerkelijk wat er bestelt is van de een naar de andere
    private Map<Facility, Map<Facility, Integer>> turnDeliver; //wat ik op deze ronde kan versturen naar de andere
    private Map<Facility, Map<Facility, Integer>> turnReceived; //turnorder maar dan de van en naar omgewisseld
    private Map<Facility, Map<Facility, Integer>> turnBackOrder; //de backorders die bij turndeliver niet verstuur dkonden worden
    private Map<Facility, Integer> stock; //de stock van iedere faciliteit
    private Map<Facility, Integer> remainingBudget; //remaining budget van iedere faciliteit*/

    public RoundFake(String gameId, int roundId) {
        super(gameId, roundId);
    }

    /*public void addTurnOrder(Map<Facility, Integer> order, Facility facilityFrom) {
        turnOrder.put(facilityFrom, order);
    }

    public void addTurnDeliver(Map<Facility, Integer> order, Facility facilityFrom) {
        turnDeliver.put(facilityFrom, order);
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

    public void addFacilityremainingStock(Integer remainingBudgetNumber, Facility facility) {
        remainingBudget.put(facility, remainingBudgetNumber);
    }*/
}
