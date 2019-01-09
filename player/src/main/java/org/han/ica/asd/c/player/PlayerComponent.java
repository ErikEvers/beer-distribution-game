package org.han.ica.asd.c.player;

import org.han.ica.asd.c.gamelogic.public_interfaces.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerComponent implements IPlayerComponent {
    private static Provider<Round> roundProvider;
    private static Provider<FacilityTurnOrder> facilityTurnOrderProvider;
    private static Provider<FacilityTurnDeliver> facilityTurnDeliverProvider;

    private Configuration configuration;
    private Facility facility;
    private Player player;
    private Round round;

    @Inject
    private IPlayerGameLogic gameLogic;

    public PlayerComponent(){}

    @Inject
	public PlayerComponent(Provider<Round> roundProvider, Provider<FacilityTurnOrder> facilityTurnOrderProvider, Provider<FacilityTurnDeliver> facilityTurnDeliverProvider) {
		this.roundProvider = roundProvider;
		this.facilityTurnOrderProvider = facilityTurnOrderProvider;
		this.facilityTurnDeliverProvider = facilityTurnDeliverProvider;
    }

	@Override
	public void activatePlayer() {
		//stub for now
	}

    @Override
    public void activateAgent() {
        //Yet to be implemented.
    }

    @Override
    public void requestFacilityUsage(Facility facility) {
        gameLogic.requestFacilityUsage(facility);
    }


    @Override
    public void selectAgent() {
        //Yet to be implemented.
    }

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

    @Override
    public List<String> getAllGames() {
        return gameLogic.getAllGames();
    }

    @Override
    public void connectToGame(String game) {
        gameLogic.connectToGame(game);
    }

    @Override
    public List<Facility> getAllFacilities() {
        return gameLogic.getAllFacilities();
    }

    @Override
    public Map<Facility, List<Facility>> seeOtherFacilities() {
        //Fake method for testing purposes
        gameLogic.seeOtherFacilities();
        Map<Facility, List<Facility>> map = new HashMap<>();

        Facility facility = new Facility(new FacilityType("Distributor", 1, 1, 1, 2, 25, 1, 1), 0);
        List<Facility> facilityList = new ArrayList<>();
        facilityList.add(new Facility(new FacilityType("Retailer", 1, 1, 1, 2, 25, 1, 2), 1));
        map.put(facility, facilityList);

        return map;
    }

    public void startNewTurn() {
        round = roundProvider.get();
    }
    
    @Override
    public void placeOrder(Facility faciliy, int amount) {
        round.setRoundId(gameLogic.getRound());
        FacilityTurnOrder facilityTurnOrder = facilityTurnOrderProvider.get();
        facilityTurnOrder.setFacilityId(player.getFacility().getFacilityId());
        facilityTurnOrder.setFacilityIdOrderTo(facility.getFacilityId());
        facilityTurnOrder.setOrderAmount(amount);
        round.getFacilityOrders().add(facilityTurnOrder);
    }

    @Override
    public void sendDelivery(Facility faciliy, int amount) {
        round.setRoundId(gameLogic.getRound());
        FacilityTurnDeliver facilityTurnDeliver = facilityTurnDeliverProvider.get();
        facilityTurnDeliver.setFacilityId(player.getFacility().getFacilityId());
        facilityTurnDeliver.setFacilityIdDeliverTo(facility.getFacilityId());
        facilityTurnDeliver.setDeliverAmount(amount);
        round.getFacilityTurnDelivers().add(facilityTurnDeliver);
    }

    public void submitTurn() {
        gameLogic.submitTurn(round);
    }

    @Override
    public void chooseFacility(Facility facility) {
        //Not implemented yet
    }

    @Override
    public String getFacilityName() {
        //Not implemented yet
        return "";
    }

    public String requestFacilityInfo(Facility facility) {
        //Fake method for testing purposes
        return "placeholderfac overview turn x\nBacklog: 25\nInventory: 0\nMoney: 500"+facility.getFacilityId();
    }

    public Facility getFacility() {
        return facility;
    }
}
