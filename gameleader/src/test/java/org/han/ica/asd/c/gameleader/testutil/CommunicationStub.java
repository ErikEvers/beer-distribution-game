package org.han.ica.asd.c.gameleader.testutil;

import org.han.ica.asd.c.exceptions.communication.TransactionException;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.gameleader.IConnectorForLeader;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.RoomModel;
import org.han.ica.asd.c.model.domain_objects.Round;

public class CommunicationStub implements IConnectorForLeader {

    @Override
    public void addObserver(IConnectorObserver observer) {

    }

    @Override
    public void sendRoundDataToAllPlayers(Round previousRound, Round currentRound) {

    }

    @Override
    public void sendGameStart(BeerGame beerGame) {

    }

    @Override
    public void sendGameEnd(BeerGame beerGame, Round previousRoundData) throws TransactionException {

    }

    @Override
    public void startRoom(RoomModel roomModel) {

    }
}
