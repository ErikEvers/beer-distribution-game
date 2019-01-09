package org.han.ica.asd.c.replay_data.fakes;

import java.util.ArrayList;
import java.util.List;

public class RoundFake {
    private int roundId;
    private List<FacilityTurnFake> facilityTurns;
    private List<FacilityTurnOrderFake> facilityOrders;
    private List<FacilityTurnDeliverFake>facilityTurnDelivers;

    public RoundFake () {
        facilityTurns = new ArrayList<>();
        facilityOrders = new ArrayList<>();
        facilityTurnDelivers = new ArrayList<>();
    }

    public RoundFake(int roundId, List<FacilityTurnFake> facilityTurns, List<FacilityTurnOrderFake> facilityOrders, List<FacilityTurnDeliverFake> facilityTurnDelivers) {
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

    public List<FacilityTurnFake> getFacilityTurns() {
        return facilityTurns;
    }

    public void setFacilityTurns(List<FacilityTurnFake> facilityTurns) {
        this.facilityTurns = facilityTurns;
    }

    public List<FacilityTurnOrderFake> getFacilityOrders() {
        return facilityOrders;
    }

    public void setFacilityOrders(List<FacilityTurnOrderFake> facilityOrders) {
        this.facilityOrders = facilityOrders;
    }

    public List<FacilityTurnDeliverFake> getFacilityTurnDelivers() {
        return facilityTurnDelivers;
    }

    public void setFacilityTurnDelivers(List<FacilityTurnDeliverFake> facilityTurnDelivers) {
        this.facilityTurnDelivers = facilityTurnDelivers;
    }
}
