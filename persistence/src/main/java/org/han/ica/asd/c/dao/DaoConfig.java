package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.exception.GameIdNotSetException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DaoConfig {
    private static String currentGameId;

    private DaoConfig() {
        //Empty method for Sonar.
    }

    public static String getCurrentGameId() {
        return currentGameId;
    }


    /**
     * Sets the current game id so that the data access objects don't require gameId as a paramater
     *
     * @param gameId
     * The game id to set
     */
    public static void setCurrentGameId(String gameId) {
        DaoConfig.currentGameId = gameId;
    }

    /**
     * Clears the game id for when the player leaves a game or stops replaying a game
     */
    public static void clearCurrentGameId() {
        DaoConfig.currentGameId = null;
    }

    /**
     * A method to check if a gameId has been set and if so adds it to the prepared statement.
     *
     * @param pstmt The prepared statement to whom the gameId needs to be added.
     * @param index The index on which place the gameId has to be set.
     */
    public static void gameIdNotSetCheck(PreparedStatement pstmt, int index) throws GameIdNotSetException, SQLException {
        if(DaoConfig.currentGameId == null) {
            throw new GameIdNotSetException("GameId not set");
        } else {
            pstmt.setString(index, DaoConfig.getCurrentGameId());
        }
    }
}
