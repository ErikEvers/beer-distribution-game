package org.han.ica.asd.c.persistence;


import org.han.ica.asd.c.dao.BeergameDAO;
import org.han.ica.asd.c.dao.GameBusinessRulesInFacilityTurnDAO;
import org.han.ica.asd.c.dao.LeaderDAO;
import org.han.ica.asd.c.dao.PlayerDAO;
import org.han.ica.asd.c.dao.RoundDAO;
import org.han.ica.asd.c.interfaces.agent.IBusinessRuleLogger;
import org.han.ica.asd.c.interfaces.gameleader.IPersistence;
import org.han.ica.asd.c.interfaces.leadermigration.IPersistenceLeaderMigration;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.GameBusinessRulesInFacilityTurn;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.model.domain_objects.Round;

import javax.inject.Inject;


public class Persistence implements IBusinessRuleLogger, IGameStore, IPersistence, IPersistenceLeaderMigration {


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


    public Persistence() {
        //Empty constructor for Guice
    }

    @Override
    public void saveFacilityTurn(Round data) {
        saveRoundData(data);
    }

    @Override
    public Round fetchFacilityTurn(int roundId) {
        return fetchRoundData(roundId);
    }

    @Override
    public void saveRoundData(Round rounddata) {
        roundDAO.createRound(rounddata);
    }

    @Override
    public void updateEndGame() {
        beergameDAO.updateEnddate();

    }

    @Override
    public Round fetchRoundData(int roundId) {
        return roundDAO.getRound(roundId);
    }

    @Override
    public BeerGame getGameLog() {
        return beergameDAO.getGameLog();
    }

    @Override
    public void saveGameLog(BeerGame beerGame, boolean isStarted) {
        if (isStarted) {
            roundDAO.createRound(beerGame.getRoundById(beerGame.getRounds().size() - 1));
        } else {
            beergameDAO.createBeergame(beerGame);
        }
    }

    @Override
    public void logUsedBusinessRuleToCreateOrder(GameBusinessRulesInFacilityTurn gameBusinessRulesInFacilityTurn) {
        gameBusinessRulesInFacilityTurnDAO.createTurn(gameBusinessRulesInFacilityTurn);
    }

    @Override
    public Player getPlayerById(String playerId) {
        return playerDAO.getPlayer(playerId);
    }

    @Override
    public void saveNewLeader(Player newLeader) {
        leaderDAO.insertLeader(newLeader);
    }
}



