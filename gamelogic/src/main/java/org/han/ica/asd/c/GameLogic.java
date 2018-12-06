package org.han.ica.asd.c;

import org.han.ica.asd.c.public_interfaces.ICommunication;

public class GameLogic {
    private ICommunication communication;

    public GameLogic(ICommunication communication) {
        this.communication = communication;
    }

    public void placeOrder(int amount) {
        communication.send(amount);
    }


}
