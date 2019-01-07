package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.exception.GameIdNotSetException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DaoConfig {
    private static String currentGameId;

    private DaoConfig(){}

    protected static String getCurrentGameId(){
        return currentGameId;
    }
    protected static void setCurrentGameId(String gameId){
        DaoConfig.currentGameId = gameId;
    }

    /**
     * A method to check if a gameId has been set and if so adds it to the prepared statement.
     *
     * @param pstmt The prepared statement to whom the gameId needs to be added.
     * @param index The index on which place the gameId has to be set.
     */
    protected static void gameIdNotSetCheck(PreparedStatement pstmt, int index) throws GameIdNotSetException, SQLException {
        if(DaoConfig.currentGameId == null) {
            throw new GameIdNotSetException("GameId not set");
        } else {
            pstmt.setString(index, DaoConfig.getCurrentGameId());
        }
    }
}
