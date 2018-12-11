package org.han.ica.asd.c;

import org.han.ica.asd.c.gameleader.componentInterfaces.IConnectorForLeader;
import org.han.ica.asd.c.gameleader.componentInterfaces.ILeaderGameLogic;
import org.han.ica.asd.c.model.Round;

public class GameLeader {
    private Round currentRoundData;
    private ILeaderGameLogic gameLogic;
    private IConnectorForLeader connectorForLeader;

    private void allTurnDataReceived() {
        this.currentRoundData = gameLogic.calculateRound(this.currentRoundData);
        connectorForLeader.sendRoundDataToAllPlayers(currentRoundData);
    }

}
