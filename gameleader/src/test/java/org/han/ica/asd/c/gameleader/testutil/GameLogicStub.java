package org.han.ica.asd.c.gameleader.testutil;

import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.gamelogic.participants.IParticipant;
import org.han.ica.asd.c.model.domain_objects.Round;

public class GameLogicStub implements ILeaderGameLogic {
	@Override
	public Round calculateRound(Round round) {
		return null;
	}

	@Override
	public void addLocalParticipant(IParticipant participant) {

	}

	@Override
	public void removeAgentByPlayerId(String playerId) {

	}
}
