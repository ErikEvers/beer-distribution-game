package org.han.ica.asd.c.interfaces.gamelogic;

import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.ProgrammedAgent;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;

import java.util.List;

public interface IConnectedForPlayer {
    boolean sendTurnData(Round turn);
    void addObserver(IConnectorObserver observer);
    void connectToGame(String game);
    void requestFacilityUsage(Facility facility);
    List<Facility> getAllFacilities();
    void sendSelectedAgent(ProgrammedAgent programmedAgent);
}