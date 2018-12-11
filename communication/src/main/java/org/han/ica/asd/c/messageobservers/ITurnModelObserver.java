package org.han.ica.asd.c.messageobservers;

import org.han.ica.asd.c.model.FacilityTurn;

public interface ITurnModelObserver extends IGameMessageObserver{
    void turnModelReceived(FacilityTurn turnModel);
}
