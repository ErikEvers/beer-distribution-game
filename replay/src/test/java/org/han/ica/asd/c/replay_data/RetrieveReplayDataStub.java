package org.han.ica.asd.c.replay_data;

import org.han.ica.asd.c.interfaces.replay.IRetrieveReplayData;
import org.han.ica.asd.c.model.domain_objects.*;

import java.util.ArrayList;
import java.util.List;

public class RetrieveReplayDataStub implements IRetrieveReplayData{
    @Override
    public List<Facility> getAllFacilities() {
        FacilityType factory = new FacilityType("factory", 1, 1, 1, 1, 1, 1, 1);
        FacilityType wholesaler = new FacilityType("wholesaler", 1, 1, 1, 1, 1, 1, 1);
        FacilityType retailer = new FacilityType("retailer", 1, 1, 1, 1, 1, 1, 1);
        FacilityType warehouse = new FacilityType("regional warehouse", 1, 1, 1, 1, 1, 1, 1);

        Facility facility1 = new Facility(factory, 1);
        Facility facility2 = new Facility(wholesaler, 2);
        Facility facility3 = new Facility(retailer, 3);
        Facility facility4 = new Facility(warehouse, 4);
        Facility facility5 = new Facility(factory, 5);
        Facility facility6 = new Facility(wholesaler, 6);
        Facility facility7 = new Facility(retailer, 7);
        Facility facility8 = new Facility(warehouse, 8);
        Facility facility9 = new Facility(warehouse, 9);


        ArrayList<Facility> returnList = new ArrayList<>();

        returnList.add(facility1);
        returnList.add(facility2);
        returnList.add(facility3);
        returnList.add(facility4);
        returnList.add(facility5);
        returnList.add(facility6);
        returnList.add(facility7);
        returnList.add(facility8);
        returnList.add(facility9);

        return returnList;
    }

    @Override
    public List<Round> getAllRounds() {
        List<Round> rounds = new ArrayList<>();

        for(int i = 0; i <= 10; i++){
            Round round = new Round();
            round.setRoundId(i);

            List<FacilityTurn> facilityTurns = new ArrayList<>();
            facilityTurns.add(new FacilityTurn(1, i, 800, 500,5, false));
            facilityTurns.add(new FacilityTurn(2, i, 40, 40,40, false));
            facilityTurns.add(new FacilityTurn(3, i, 200, 200,200, false));
            facilityTurns.add(new FacilityTurn(4, i, 50, 50,50, false));
            facilityTurns.add(new FacilityTurn(5, i, 800, 500,15, false));
            facilityTurns.add(new FacilityTurn(6, i, 40, 40,40, false));
            facilityTurns.add(new FacilityTurn(7, i, 200, 200,200, false));
            facilityTurns.add(new FacilityTurn(8, i, 100, 100,100, false));
            facilityTurns.add(new FacilityTurn(9, i, 150, 150,150, false));

            round.setFacilityTurns(facilityTurns);

            List<FacilityTurnDeliver> facilityTurnDelivers = new ArrayList<>();
            facilityTurnDelivers.add(new FacilityTurnDeliver(1, 2, 5, 3000));
            facilityTurnDelivers.add(new FacilityTurnDeliver(2, 3, 5, 5));
            facilityTurnDelivers.add(new FacilityTurnDeliver(3, 4, 5, 5));
            facilityTurnDelivers.add(new FacilityTurnDeliver(4, 1, 5, 5));
            facilityTurnDelivers.add(new FacilityTurnDeliver(5, 1, 5, 1000));
            facilityTurnDelivers.add(new FacilityTurnDeliver(6, 1, 5, 5));
            facilityTurnDelivers.add(new FacilityTurnDeliver(7, 1, 5, 5));
            facilityTurnDelivers.add(new FacilityTurnDeliver(8, 1, 5, 5));
            facilityTurnDelivers.add(new FacilityTurnDeliver(9, 1, 5, 5));

            round.setFacilityTurnDelivers(facilityTurnDelivers);

            List<FacilityTurnOrder> facilityTurnOrders = new ArrayList<>();
            facilityTurnOrders.add(new FacilityTurnOrder(1, 4, 1000));
            facilityTurnOrders.add(new FacilityTurnOrder(2, 1, 5));
            facilityTurnOrders.add(new FacilityTurnOrder(3, 2, 5));
            facilityTurnOrders.add(new FacilityTurnOrder(4, 3, 5));
            facilityTurnOrders.add(new FacilityTurnOrder(5, 3, 1000));
            facilityTurnOrders.add(new FacilityTurnOrder(6, 3, 5));
            facilityTurnOrders.add(new FacilityTurnOrder(7, 3, 5));
            facilityTurnOrders.add(new FacilityTurnOrder(8, 3, 5));
            facilityTurnOrders.add(new FacilityTurnOrder(9, 3, 5));

            round.setFacilityOrders(facilityTurnOrders);

            rounds.add(round);
        }
        return rounds;
    }
}
