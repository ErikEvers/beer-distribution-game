package org.han.ica.asd.c.player;

import org.han.ica.asd.c.agent.Agent;
import org.han.ica.asd.c.gamelogic.public_interfaces.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRules;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.ProgrammedBusinessRules;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerComponent implements IPlayerComponent {
    private Provider<Round> roundProvider;
    private Provider<FacilityTurnOrder> facilityTurnOrderProvider;
    private Provider<FacilityTurnDeliver> facilityTurnDeliverProvider;

    private Configuration configuration;

    private static Player player;
    private Round round;

    @Inject
    private IPlayerGameLogic gameLogic;

    @Inject
    @Named("BusinessruleStore")
    IBusinessRuleStore iBusinessRuleStore;

    @Inject
    @Named("GameLogic")
    IPlayerGameLogic iPlayerGameLogic;

    @Inject
	public PlayerComponent(Provider<Round> roundProvider, Provider<FacilityTurnOrder> facilityTurnOrderProvider, Provider<FacilityTurnDeliver> facilityTurnDeliverProvider) {
		this.roundProvider = roundProvider;
		this.facilityTurnOrderProvider = facilityTurnOrderProvider;
		this.facilityTurnDeliverProvider = facilityTurnDeliverProvider;
    }

	@Override
	public void activatePlayer() {
        iPlayerGameLogic.letPlayerTakeOverAgent();
	}

    @Override
    public void activateGameAgent(String agentName, Facility facility) {
        ProgrammedAgent programmedAgent = iBusinessRuleStore.getProgrammedGameAgent(agentName);

        List <ProgrammedBusinessRules> programmedBusinessRules = programmedAgent.getProgrammedBusinessRules();

        List<GameBusinessRules> gameBusinessRules = programmedBusinessRules
                .stream()
                .map(programmedBusinessRule -> new GameBusinessRules(programmedBusinessRule.getProgrammedBusinessRule(), programmedBusinessRule.getProgrammedAST()))
                .collect(Collectors.toList());

        Agent agent = new Agent(configuration, agentName, facility, gameBusinessRules);

        iPlayerGameLogic.letAgentTakeOverPlayer(agent);
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
        round.setRoundId(gameLogic.getRound());
    }
    
    @Override
    public void placeOrder(Facility facility, int amount) {

        Optional<FacilityTurnOrder> facilityTurnOrderOptional = round.getFacilityOrders().stream().filter(facilityTurnDeliver -> facilityTurnDeliver.getFacilityId() == player.getFacility().getFacilityId() && facilityTurnDeliver.getFacilityIdOrderTo() == facility.getFacilityId()).findFirst();
        if(!facilityTurnOrderOptional.isPresent()) {
            FacilityTurnOrder facilityTurnOrder = facilityTurnOrderProvider.get();
            facilityTurnOrder.setFacilityId(player.getFacility().getFacilityId());
            facilityTurnOrder.setFacilityIdOrderTo(facility.getFacilityId());
            facilityTurnOrder.setOrderAmount(amount);
            round.getFacilityOrders().add(facilityTurnOrder);
            gameLogic.submitTurn(round);
        } else {
            facilityTurnOrderOptional.get().setOrderAmount(facilityTurnOrderOptional.get().getOrderAmount() + amount);
        }
    }

    @Override
    public void sendDelivery(Facility facility, int amount) {
        Optional<FacilityTurnDeliver> facilityTurnDeliverOptional = round.getFacilityTurnDelivers().stream().filter(facilityTurnDeliver -> facilityTurnDeliver.getFacilityId() == player.getFacility().getFacilityId() && facilityTurnDeliver.getFacilityIdDeliverTo() == facility.getFacilityId()).findFirst();
        if(!facilityTurnDeliverOptional.isPresent()) {
            FacilityTurnDeliver facilityTurnDeliver = facilityTurnDeliverProvider.get();
            facilityTurnDeliver.setFacilityId(player.getFacility().getFacilityId());
            facilityTurnDeliver.setFacilityIdDeliverTo(facility.getFacilityId());
            facilityTurnDeliver.setDeliverAmount(amount);
            round.getFacilityTurnDelivers().add(facilityTurnDeliver);
            gameLogic.submitTurn(round);
        } else {
            facilityTurnDeliverOptional.get().setDeliverAmount(facilityTurnDeliverOptional.get().getDeliverAmount() + amount);
        }
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
        return player.getFacility();
    }
}
