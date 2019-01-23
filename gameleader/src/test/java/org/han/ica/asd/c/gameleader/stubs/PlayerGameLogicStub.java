package org.han.ica.asd.c.gameleader.stubs;

import org.han.ica.asd.c.interfaces.gamelogic.IParticipant;
import org.han.ica.asd.c.interfaces.gamelogic.IPlayerGameLogic;
import org.han.ica.asd.c.interfaces.player.IPlayerRoundListener;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.GameAgent;
import org.han.ica.asd.c.model.domain_objects.Round;

import java.util.List;

public class PlayerGameLogicStub implements IPlayerGameLogic {
    @Override
    public void submitTurn(Round turn) {

    }

    @Override
    public BeerGame getBeerGame() {
        return null;
    }

    @Override
    public void letAgentTakeOverPlayer(GameAgent agent) {

    }

    @Override
    public void letPlayerTakeOverAgent() {

    }

    public List<String> getAllGames() {
        return null;
    }

    public void connectToGame(String game) {

    }
    public void requestFacilityUsage(Facility facility) {

    }

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

    @Override
    public void setLastTurn(Round lastround) {

    }
}
