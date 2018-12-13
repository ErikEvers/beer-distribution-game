package org.han.ica.asd.c.gameleader;

import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.observers.IPlayerDisconnectedObserver;

import javax.inject.Inject;

public class GameLeader implements IPlayerDisconnectedObserver {

    @Inject
    ILeaderGameLogic gameLogic;

    @Override
    public void notifyPlayerDisconnected(String playerId) {
        IParticipant participant = new AgentParticipant();
        gameLogic.addLocalParticipant(participant);
    }

    public void notifyPlayerReconnected(String playerId) {
        gameLogic.removeAgentByPlayerId(playerId);
    }
}
