package org.han.ica.asd.c.replay_data.fakes;

import org.han.ica.asd.c.interfaces.replay.IRetrieveReplayData;
import org.han.ica.asd.c.model.domain_objects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TMP implements IRetrieveReplayData{
    @Override
    public List<Facility> getAllFacilities() {
        FacilityType factory = new FacilityType("Factory", 1, 1, 1, 1, 1, 1, 1);
        FacilityType wholesaler = new FacilityType("Wholesaler", 1, 1, 1, 1, 1, 1, 1);
        FacilityType retailer = new FacilityType("Retailer", 1, 1, 1, 1, 1, 1, 1);
        FacilityType warehouse = new FacilityType("Warehouse", 1, 1, 1, 1, 1, 1, 1);

        Facility facility1 = new Facility(factory, 1);
        Facility facility2 = new Facility(wholesaler, 2);
        Facility facility3 = new Facility(retailer, 3);
        Facility facility4 = new Facility(warehouse, 4);

        ArrayList<Facility> returnList = new ArrayList<>();

        returnList.add(facility1);
        returnList.add(facility2);
        returnList.add(facility3);
        returnList.add(facility4);

        return returnList;
    }

    @Override
    public List<Round> getAllRounds() {
        List<Round> rounds = new ArrayList<>();

        Random rand = new Random();
        for(int i = 0; i < rand.nextInt(10)+5; i++){
            Round round = new Round();
            round.setRoundId(i);

            List<FacilityTurn> facilityTurns = new ArrayList<>();
            facilityTurns.add(new FacilityTurn(1, i, rand.nextInt(200)-100, rand.nextInt(200)-100,rand.nextInt(200)-100, false));
            facilityTurns.add(new FacilityTurn(2, i, rand.nextInt(200)-100, rand.nextInt(200)-100,rand.nextInt(200)-100, false));
            facilityTurns.add(new FacilityTurn(3, i, rand.nextInt(200)-100, rand.nextInt(200)-100,rand.nextInt(200)-100, false));
            facilityTurns.add(new FacilityTurn(4, i, rand.nextInt(200)-100, rand.nextInt(200)-100,rand.nextInt(200)-100, false));

            round.setFacilityTurns(facilityTurns);

            List<FacilityTurnDeliver> facilityTurnDelivers = new ArrayList<>();
            facilityTurnDelivers.add(new FacilityTurnDeliver(1, 2, rand.nextInt(200)-100, rand.nextInt(200)-100));
            facilityTurnDelivers.add(new FacilityTurnDeliver(2, 3, rand.nextInt(200)-100, rand.nextInt(200)-100));
            facilityTurnDelivers.add(new FacilityTurnDeliver(3, 4, rand.nextInt(200)-100, rand.nextInt(200)-100));
            facilityTurnDelivers.add(new FacilityTurnDeliver(4, 1, rand.nextInt(200)-100, rand.nextInt(200)-100));

            round.setFacilityTurnDelivers(facilityTurnDelivers);

            List<FacilityTurnOrder> facilityTurnOrders = new ArrayList<>();
            facilityTurnOrders.add(new FacilityTurnOrder(1, 4, rand.nextInt(200)-100));
            facilityTurnOrders.add(new FacilityTurnOrder(2, 1, rand.nextInt(200)-100));
            facilityTurnOrders.add(new FacilityTurnOrder(3, 2, rand.nextInt(200)-100));
            facilityTurnOrders.add(new FacilityTurnOrder(4, 3, rand.nextInt(200)-100));

            round.setFacilityOrders(facilityTurnOrders);

            rounds.add(round);
        }
        return rounds;
    }
}
