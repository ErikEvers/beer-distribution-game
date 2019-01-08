package org.han.ica.asd.c.interfaces.communication;



import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.RoomModel;

import java.util.List;

public interface IConnecterForSetup {
    List<String> getAvailableRooms();
    RoomModel createRoom(String roomName, String password);
    RoomModel joinRoom(String roomName, String password);
    RoomModel updateRoom(RoomModel room);
    void removeHostFromRoom(RoomModel room, String hostIP);
    void removeYourselfFromRoom(RoomModel room);
    void startRoom(RoomModel room);
    void addObserver(IConnectorObserver connectorObserver);
    void chooseFacility(Facility facility) throws Exception;
    List<Facility> getAllFacilities();
}
