package org.han.ica.asd.c.gameleader.componentInterfaces;
import org.han.ica.asd.c.model.Round;
import org.han.ica.asd.c.observers.IConnectorObserver;

/**
 * Interface for the communication component
 * which is responsible for sending and receiving information, and maintaining connections throughout a game.
 */
public interface IConnectorForLeader {

    /**
     * Register an instance of a IMessageObserver as an observer that listens to the communication component.
     * @param observer, instance of IMessageObserver.
     */
    public void addObserver(IConnectorObserver observer);

    /**
     * The data of a specific round gets sent to the participants of said game.
     * @param allData, the game data of a specific round.
     */
    public void sendRoundDataToAllPlayers(Round allData);
}
