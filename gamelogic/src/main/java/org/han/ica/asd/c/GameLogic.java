package org.han.ica.asd.c;

import org.han.ica.asd.c.exceptions.RoundDataNotFoundException;
import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.GameAgent;
import org.han.ica.asd.c.model.Round;
import org.han.ica.asd.c.participants.ParticipantsPool;
import org.han.ica.asd.c.participants.domain_models.AgentParticipant;
import org.han.ica.asd.c.public_interfaces.ICommunication;
import org.han.ica.asd.c.public_interfaces.IPersistence;
import org.han.ica.asd.c.public_interfaces.IPlayerGameLogic;

import java.util.List;

public class GameLogic implements IPlayerGameLogic {
    String gameId;
    private ICommunication communication;
    private IPersistence persistence;
    private ParticipantsPool participantsPool;
    int round;

    public GameLogic(String gameId, ICommunication communication, IPersistence persistence, ParticipantsPool participantsPool) {
        this.gameId = gameId;
        this.communication = communication;
        this.persistence = persistence;
        this.participantsPool = participantsPool;
        this.round = 0;
    }

    @Override
    public void placeOrder(FacilityTurn turn) {
        persistence.saveTurnData(turn);
        communication.sendTurnData(turn);
    }

    @Override
    public Round seeOtherFacilities() {
        return persistence.fetchRoundData(gameId, round);
    }

    @Override
    public void letAgentTakeOverPlayer(AgentParticipant agent) {
        participantsPool.removeParticipant(agent);
    }
}
