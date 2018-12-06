package org.han.ica.asd.c.gameleader.componentInterfaces;

/**
 * interface for persistence component
 * which is responsible for storing and fetching data to and from a persistent data store.
 */
public interface IPersistence {

    /**
     * Store the actions of a player in a specific round.
     * @param data, the info to be saved.
     */
    public void savePlayerTurn(PlayerTurn data);

    /**
     * Retrieve the data of a players turn.
     * @param roundId, the identifier of the turn to retrieve.
     * @param playerId, the identifier of the player of which the round actions are to be retrieved.
     * @return the round data for that player in that round.
     */
    public PlayerTurn fetchPlayerTurn(int roundId, String playerId);

    /**
     * Store the actions of the game up until a specific round.
     * @param data, the info to be saved.
     */
    public void saveRoundData(RoundData data);

    /**
     * The data of the game up until a specific round gets fetched by it's id.
     * @param roundId, the id of the round that has to be fetched.
     * @return the game data at that round.
     */
    public RoundData fetchRoundData(int roundId);
}
