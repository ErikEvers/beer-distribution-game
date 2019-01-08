package org.han.ica.asd.c.persistence;


import org.han.ica.asd.c.dao.BeergameDAO;
import org.han.ica.asd.c.dao.GameBusinessRulesInFacilityTurnDAO;
import org.han.ica.asd.c.dao.PlayerDAO;
import org.han.ica.asd.c.dao.RoundDAO;
import org.han.ica.asd.c.interfaces.agent.IBusinessRuleLogger;
import org.han.ica.asd.c.interfaces.gamelogic.IRoundStore;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.FacilityTurn;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.FacilityType;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Persistence implements IRoundStore, IBusinessRuleLogger, IGameStore {

	@Inject
	private RoundDAO roundDAO;

	@Inject
	private BeergameDAO beergameDAO;

	@Inject
	private GameBusinessRulesInFacilityTurnDAO gameBusinessRulesInFacilityTurnDAO;

	@Inject
	private PlayerDAO playerDAO;


	public Persistence(){
		//Empty constructor for Guice
	}

	@Override
	public void saveRoundData(Round rounddata)
	{
		roundDAO.createRound(rounddata.getRoundId());
	}


	@Override
	public Round fetchRoundData(int roundId) {
		return roundDAO.getRound(roundId);
	}

	@Override
	public BeerGame getGameLog()
	{
		return beergameDAO.getGameLog();
	}

	@Override
	public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn)
	{
		gameBusinessRulesInFacilityTurnDAO.createTurn(gameBusinessRulesInFacilityTurn);
	}


	@Override
	public Player getPlayerById(String playerId) {
		return playerDAO.getPlayer(playerId);
	}

	@Override
	public BeerGame getCurrentBeerGame() {
		Configuration configuration = new Configuration();

		Facility retailer = new Facility(new FacilityType("Retailer", 0, 0,0,0,0,0, 0), 0);
		Facility wholesale = new Facility(new FacilityType("Wholesaler", 0, 0,0,0,0,0, 0), 1);
		Facility warehouse = new Facility(new FacilityType("Regional Warehouse", 0, 0,0,0,0,0, 0), 2);
		Facility factory = new Facility(new FacilityType("Factory", 0, 0,0,0,0,0, 0), 3);

		List<Facility> facilityList = new ArrayList<>();
		facilityList.add(retailer);
		facilityList.add(wholesale);
		facilityList.add(warehouse);
		facilityList.add(factory);

		configuration.setFacilities(facilityList);

		Map<Facility, List<Facility>> links = new HashMap<>();
		List<Facility> list = new ArrayList<>();
		list.add(wholesale);
		links.put(retailer, list);

		list = new ArrayList<>();
		list.add(warehouse);
		links.put(wholesale, list);

		list = new ArrayList<>();
		list.add(factory);
		links.put(warehouse, list);

		configuration.setFacilitiesLinkedTo(links);

		configuration.setAmountOfWarehouses(1);
		configuration.setAmountOfFactories(1);
		configuration.setAmountOfWholesalers(1);
		configuration.setAmountOfRetailers(1);

		configuration.setAmountOfRounds(20);

		configuration.setContinuePlayingWhenBankrupt(false);

		configuration.setInsightFacilities(true);

		configuration.setMaximumOrderRetail(99);
		configuration.setMinimalOrderRetail(5);

		BeerGame beerGame = new BeerGame();
		beerGame.setConfiguration(configuration);
		Player henk = new Player("1", "111", retailer, "Henk", true);
		beerGame.getPlayers().add(henk);
		beerGame.getAgents().add(new GameAgent("wholesaleAgent", wholesale, new ArrayList<>()));
		beerGame.getAgents().add(new GameAgent("warehouseAgent", warehouse, new ArrayList<>()));
		beerGame.getAgents().add(new GameAgent("factoryAgent", factory, new ArrayList<>()));
		beerGame.setLeader(new Leader(henk));
		beerGame.setGameId("123");
		beerGame.setGameName("Henks spel");
		beerGame.setGameDate("2019-01-08");
		
		return beerGame;
	}


	@Override
	public void saveTurnData(Round round)
	{
		roundDAO.createRound(round.getRoundId());
		for (FacilityTurn facilityTurn: round.getFacilityTurns()) {
			roundDAO.createFacilityTurn(round.getRoundId(),facilityTurn);
		}

		for (FacilityTurnOrder facilityTurnOrder: round.getFacilityOrders()) {
			roundDAO.createFacilityOrder(round.getRoundId(),facilityTurnOrder);
		}

		for (FacilityTurnDeliver facilityTurnDeliver: round.getFacilityTurnDelivers()) {
			roundDAO.createFacilityDeliver(round.getRoundId(),facilityTurnDeliver);
		}
	}
}
