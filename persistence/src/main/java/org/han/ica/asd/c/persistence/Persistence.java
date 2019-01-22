package org.han.ica.asd.c.persistence;


import org.han.ica.asd.c.dao.BeergameDAO;
import org.han.ica.asd.c.dao.FacilityDAO;
import org.han.ica.asd.c.dao.GameBusinessRulesInFacilityTurnDAO;
import org.han.ica.asd.c.dao.LeaderDAO;
import org.han.ica.asd.c.dao.PlayerDAO;
import org.han.ica.asd.c.dao.RoundDAO;
import org.han.ica.asd.c.interfaces.agent.IBusinessRuleLogger;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.leadermigration.IPersistenceLeaderMigration;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.interfaces.replay.IRetrieveReplayData;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.model.domain_objects.*;

import javax.inject.Inject;
import java.util.List;



public class Persistence implements IBusinessRuleLogger, IGameStore, IPersistence, IPersistenceLeaderMigration, IRetrieveReplayData {


    @Inject
    private RoundDAO roundDAO;

    @Inject
    private BeergameDAO beergameDAO;

    @Inject
    private GameBusinessRulesInFacilityTurnDAO gameBusinessRulesInFacilityTurnDAO;

    @Inject
    private PlayerDAO playerDAO;

    @Inject
    private LeaderDAO leaderDAO;

	@Inject
	private FacilityDAO facilityDAO;


    public Persistence() {
        //Empty constructor for Guice
    }

    @Override
    public synchronized void saveFacilityTurn(Round data) {
        saveRoundData(data);
    }

    @Override
    public synchronized Round fetchFacilityTurn(int roundId) {
        return fetchRoundData(roundId);
    }

    @Override
    public synchronized void saveRoundData(Round rounddata) {
        roundDAO.createRound(rounddata);
    }

    @Override
    public synchronized void updateEndGame() {
        beergameDAO.updateEnddate();

    }

    @Override
    public synchronized Round fetchRoundData(int roundId) {
        return roundDAO.getRound(roundId);
    }

    @Override
    public synchronized BeerGame getGameLog() {
        return beergameDAO.getGameLog();
    }

    @Override
    public synchronized void saveGameLog(BeerGame beerGame, boolean isStarted) {
        if (isStarted) {
            roundDAO.createRound(beerGame.getRoundById(beerGame.getRounds().size() - 1));
        } else {
            beergameDAO.createBeergame(beerGame);
        }
    }

    @Override
    public synchronized void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {
        gameBusinessRulesInFacilityTurnDAO.createTurn(gameBusinessRulesInFacilityTurn);
    }

    @Override
    public synchronized Player getPlayerById(String playerId) {
        return playerDAO.getPlayer(playerId);
    }

	@Override
	public synchronized void saveSelectedAgent(ProgrammedAgent agent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized void saveNewLeader(Player newLeader) {
		leaderDAO.insertLeader(new Leader(newLeader));
	}

	@Override
	public synchronized void updateRound(Round round) {
		roundDAO.updateRound(round);
	}

	@Override
	public synchronized void createRound(Round round) {
		roundDAO.createRound(round);
	}


	/**
	 * @inheritDoc
	 */
	@Override
	public synchronized List<Facility> getAllFacilities() {
		return facilityDAO.readAllFacilitiesInGame();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public synchronized List<BeerGame> getAllBeerGames(){
		return beergameDAO.readBeergames();
	}

}



