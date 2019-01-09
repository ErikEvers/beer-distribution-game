package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.GamePlayerId;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.REQUEST_GAME_DATA_MESSAGE;

public class RequestGameDataMessage extends GameMessage{
    private GamePlayerId gameData;

    public RequestGameDataMessage() {
        super(REQUEST_GAME_DATA_MESSAGE);
    }

    public GamePlayerId getGameData() {
        return gameData;
    }

    public void setGameData(GamePlayerId gameData) {
        this.gameData = gameData;
    }
}