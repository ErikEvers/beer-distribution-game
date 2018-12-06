package org.han.ica.asd.c.gameleader.componentInterfaces;

/**
 * interface for communication component
 * which is responsible for sending and receiving information, and maintaining connections throughout a game.
 */
public interface ICommunication {

    /**
     * The data of the game up until a specific round gets sent to the participants of said game.
     * @param data, the game data up until that point.
     */
    public void sendRoundData(RoundData data);
}
