package org.han.ica.asd.c;

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
        persistence.saveOrder(amount);
    }
}
