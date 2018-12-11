package org.han.ica.asd.c;

import org.han.ica.asd.c.model.Round;

public interface ILeaderGameLogic {
    Round calculateRound(Round round);
    void addLocalParticipant(IParticipant participant);
    void removeAgentByPlayerId(String playerId);
}
