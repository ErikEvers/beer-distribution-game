package org.han.ica.asd.c;

import org.han.ica.asd.c.model.domain_objects.RoomModel;

public class Main {
    public static void main(String[] args) {
        Connector c = new Connector();

        String roomName = "Test1337";

        RoomModel created = c.createRoom(roomName, "");

        for (String room:c.getAvailableRooms()){
            System.out.println(room);
        }
        c.startRoom(created);

        RoomModel joined = c.joinRoom("Beergame 1", "");
        for (String host:joined.getHosts()){
            System.out.println(host);
        }
        c.removeYourselfFromRoom(joined);
    }
}
