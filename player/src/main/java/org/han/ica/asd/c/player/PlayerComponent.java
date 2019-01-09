package org.han.ica.asd.c.player;

import org.han.ica.asd.c.gamelogic.public_interfaces.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Player;
import javax.inject.Inject;
import org.han.ica.asd.c.model.domain_objects.*;
import javax.inject.Provider;
import java.util.List;

public class PlayerComponent implements IPlayerComponent {
    private static Provider<Round> roundProvider;
    private static Provider<FacilityTurnOrder> facilityTurnOrderProvider;
    private static Provider<FacilityTurnDeliver> facilityTurnDeliverProvider;

    private Configuration configuration;
    private Facility facility;
    private static Player player;
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
    public BeerGame seeOtherFacilities() {
        return gameLogic.seeOtherFacilities();
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
        //comm.chooseFacility(facility, player.getPlayerId());
    }

    @Override
    public String getFacilityName() {
        return player.getFacility().getFacilityType().getFacilityName();
    }

    public static void setPlayer(Player player) {
        PlayerComponent.player = player;
    }

    public static Player getPlayer() {
        return player;
    }

    public Facility getFacility() {
        return facility;
    }
}
