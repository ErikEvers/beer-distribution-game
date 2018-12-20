package org.han.ica.asd.c.dao;

public class DaoConfig {
    private static String currentGameId;

    private DaoConfig(){}

    protected static String getCurrentGameId(){
        return currentGameId;
    }
    protected static void setCurrentGameId(String gameId){
        DaoConfig.currentGameId = gameId;
    }
}
