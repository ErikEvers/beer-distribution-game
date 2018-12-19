import org.han.ica.asd.c.Connector;
import org.han.ica.asd.c.discovery.DiscoveryException;
import org.han.ica.asd.c.discovery.Room;
import org.han.ica.asd.c.discovery.RoomException;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;

public class Main {
    public static void main(String[] args) throws DiscoveryException, RoomException {
        Connector c = Connector.getInstance();

        System.out.println("All rooms.");
        for (String room:c.getAvailableRooms()){
            System.out.println(room);
        }
        String roomName = "Beergame 500";
        String leaderIP = "192.168.0.30";
        String hostIP = "102.168.0.40";
        String hostIP2 = "192.168.3.2";
        String password = "";

        Room createdRoom = c.createRoom(roomName, leaderIP, password);

        c.joinRoom(roomName, hostIP, password);
        c.joinRoom(roomName, hostIP2, password);
        createdRoom.updateHosts();

        c.startRoom(createdRoom);

        NodeInfoList ips = c.getIps();
        System.out.println(ips.get(0).getIp());
        System.out.println(ips.get(1).getIp());
        System.out.println(ips.get(2).getIp());

        c.setJoiner();
    }
}
