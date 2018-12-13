package org.han.ica.asd.c.gameleader.componentInterfaces;

import org.han.ica.asd.c.model.Round;

public interface ILeaderGameLogic {
    Round calculateRound(Round round);

    void addLocalParticipant(IParticipant participant);

    void removeAgentByPlayerId(String playerId);
}
