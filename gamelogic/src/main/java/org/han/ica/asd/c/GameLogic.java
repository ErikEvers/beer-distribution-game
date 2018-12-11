package org.han.ica.asd.c;

import org.han.ica.asd.c.domain.Facility;
import org.han.ica.asd.c.domain.Order;
import org.han.ica.asd.c.domain.PlayerRoundData;
import org.han.ica.asd.c.domain.RoundData;
import org.han.ica.asd.c.exceptions.RoundDataNotFoundException;
import org.han.ica.asd.c.public_interfaces.ICommunication;
import org.han.ica.asd.c.public_interfaces.IPersistence;

public class GameLogic {
    private ICommunication communication;
    private IPersistence persistence;

    public GameLogic(ICommunication communication, IPersistence persistence) {
        this.communication = communication;
        this.persistence = persistence;
    }

    public void placeOrder(int amount) {
        communication.send(amount);
        persistence.saveOrder(new Order(amount));
    }

    public RoundData getRoundDataFromFacility(Facility facility) throws RoundDataNotFoundException {
        RoundData roundData = persistence.getRoundData(facility);
        if (roundData == null) {
            throw new RoundDataNotFoundException();
        }
        return roundData;
    }
}
