package org.han.ica.asd.c.dao;

import org.han.ica.asd.c.exception.GameIdNotSetException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DaoConfig {
    private static String currentGameId;

    protected static String getCurrentGameId(){
        return currentGameId;
    }
    protected static void setCurrentGameId(String gameId){
        DaoConfig.currentGameId = gameId;
    }


}
