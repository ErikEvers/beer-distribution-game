package org.han.ica.asd.c.gameleader.componentInterfaces;

/**
 * interface for the communication component
 * which is responsible for sending and receiving information, and maintaining connections throughout a game.
 */
public interface ICommunication {

    /**
     * Register an instance of a IMessageObserver as an observer that listens to the communication component.
     * @param observer, instance of IMessageObserver.
     */
    public void addObserver(IMessageObserver observer);

    /**
     * The data of a specific round gets sent to the participants of said game.
     * @param data, the game data of a specific round.
     */
    public void sendRoundDataToAllPlayers(RoundModel allData);
}
