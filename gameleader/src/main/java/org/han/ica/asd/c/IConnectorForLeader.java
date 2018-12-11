package org.han.ica.asd.c;

import org.han.ica.asd.c.model.Round;

public interface IConnectorForLeader {
    void addObserver(IMessageObserver observer);
    void sendRoundDataToAllPlayers(Round allData);
}
