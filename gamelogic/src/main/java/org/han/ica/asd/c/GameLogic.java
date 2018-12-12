package org.han.ica.asd.c;

import org.han.ica.asd.c.exceptions.RoundDataNotFoundException;
import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.FacilityTurn;
import org.han.ica.asd.c.public_interfaces.ICommunication;
import org.han.ica.asd.c.public_interfaces.IPersistence;
import org.han.ica.asd.c.public_interfaces.IPlayerGameLogic;

import java.util.List;

public class GameLogic implements IPlayerGameLogic {
    String gameId;
    private ICommunication communication;
    private IPersistence persistence;
    int round;

    public GameLogic(String gameId, ICommunication communication, IPersistence persistence) {
        this.gameId = gameId;
        this.communication = communication;
        this.persistence = persistence;
        this.round = 0;
    }
}
