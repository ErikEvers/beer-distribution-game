package org.han.ica.asd.c.agent.stubs;

import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.interfaces.gamelogic.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.player.IPlayerRoundListener;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;

public class GameLogicStub implements IPlayerGameLogic {
    @Override
    public void submitTurn(Round turn) {

    }

    @Override
    public BeerGame getBeerGame() {
        return null;
    }

    @Override
    public void letAgentTakeOverPlayer(IParticipant agent) {

    }

    @Override
    public void letPlayerTakeOverAgent() {

    }

    @Override
    public List<String> getAllGames() {
        return null;
    }

    @Override
    public void connectToGame(String game) {

    }

    @Override
    public void requestFacilityUsage(Facility facility) {

    }

    @Override
    public List<Facility> getAllFacilities() {
        return null;
    }

    @Override
    public int getRoundId() {
        return 0;
    }

    @Override
    public void setPlayer(IPlayerRoundListener player) {

    }
}
