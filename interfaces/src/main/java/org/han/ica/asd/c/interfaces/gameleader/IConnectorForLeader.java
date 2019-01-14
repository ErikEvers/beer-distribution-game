package org.han.ica.asd.c.interfaces.gameleader;

import org.han.ica.asd.c.exceptions.communication.TransactionException;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.RoomModel;
import org.han.ica.asd.c.model.domain_objects.Round;

/**
 * Interface for the communication component
 * which is responsible for sending and receiving information, and maintaining connections throughout a game.
 */
public interface IConnectorForLeader {

    /**
     * Register an instance of a IMessageObserver as an observer that listens to the communication component.
     * @param observer, instance of IMessageObserver.
     */
    void addObserver(IConnectorObserver observer);

    /**
     * The data of a specific round gets sent to the participants of said game.
     * @param newRound, the game data of a specific round.
     */
    void sendRoundDataToAllPlayers(Round previousRound, Round newRound, BeerGame beerGame) throws TransactionException;

    void sendGameStart(BeerGame beerGame) throws TransactionException;

    void startRoom(RoomModel roomModel);
}
