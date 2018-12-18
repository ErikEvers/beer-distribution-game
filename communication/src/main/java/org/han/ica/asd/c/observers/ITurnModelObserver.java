package org.han.ica.asd.c.observers;

import org.han.ica.asd.c.model.dao_model.FacilityTurn;

public interface ITurnModelObserver extends IConnectorObserver{
    void turnModelReceived(FacilityTurn turnModel);
}
