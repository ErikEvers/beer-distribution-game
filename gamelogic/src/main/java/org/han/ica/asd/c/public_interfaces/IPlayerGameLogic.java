package org.han.ica.asd.c.public_interfaces;

import org.han.ica.asd.c.domain.Facility;
import org.han.ica.asd.c.domain.RoundData;
import org.han.ica.asd.c.model.FacilityTurn;

import java.util.List;

public interface IPlayerGameLogic {
    void placeOrder(FacilityTurn turn);

    List<RoundData> getHistoryFromFacility(Facility facility);
}
