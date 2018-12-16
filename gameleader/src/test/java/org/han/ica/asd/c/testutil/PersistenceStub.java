package org.han.ica.asd.c.testutil;

import org.han.ica.asd.c.gameleader.componentInterfaces.IPersistence;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.model.Round;

public class PersistenceStub implements IPersistence{

    @Override
    public void savePlayerTurn(FacilityTurn data) {

    }

    @Override
    public FacilityTurn fetchPlayerTurn(int roundId, int facilityId) {
        return null;
    }

    @Override
    public void saveRoundData(Round data) {

    }

    @Override
    public Round fetchRoundData(int roundId) {
        return null;
    }
}
