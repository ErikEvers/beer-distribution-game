package org.han.ica.asd.c.model.domain_objects;


import java.util.ArrayList;
import java.util.List;

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

    public Round(int roundId, List<FacilityTurn> facilityTurns, List<FacilityTurnOrder> facilityOrders, List<FacilityTurnDeliver> facilityTurnDelivers) {
        this.roundId = roundId;
        this.facilityTurns = facilityTurns;
        this.facilityOrders = facilityOrders;
        this.facilityTurnDelivers = facilityTurnDelivers;
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
