package org.han.ica.asd.c.public_interfaces;

import org.han.ica.asd.c.domain.Facility;
import org.han.ica.asd.c.domain.Order;
import org.han.ica.asd.c.domain.RoundData;

import java.util.List;

public interface IPersistence {
    void saveOrder(Order order);

    RoundData getRoundData(Facility facility);

    List<RoundData> getHistory(Facility facility);
}
