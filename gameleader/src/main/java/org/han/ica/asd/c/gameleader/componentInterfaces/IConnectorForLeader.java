package org.han.ica.asd.c.gameleader.componentInterfaces;

import org.han.ica.asd.c.model.Round;

public interface IConnectorForLeader {
    void sendRoundDataToAllPlayers(Round allData);
}
