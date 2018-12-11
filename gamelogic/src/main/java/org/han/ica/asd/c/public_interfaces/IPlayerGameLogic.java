package org.han.ica.asd.c.public_interfaces;

import org.han.ica.asd.c.domain.Facility;
import org.han.ica.asd.c.domain.RoundData;

import java.util.List;

public interface IPlayerGameLogic {
    void placeOrder(int amount);

    List<RoundData> getHistoryFromFacility(Facility facility);
}
