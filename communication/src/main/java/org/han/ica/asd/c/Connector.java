package org.han.ica.asd.c;

import org.han.ica.asd.c.discovery.DiscoveryException;
import org.han.ica.asd.c.discovery.IFinder;
import org.han.ica.asd.c.discovery.RoomFinder;
import org.han.ica.asd.c.model.domain_objects.RoomModel;
import org.han.ica.asd.c.faultdetection.FaultDetectionClient;
import org.han.ica.asd.c.faultdetection.FaultDetector;
import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfo;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnecterForSetup;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.messagehandler.receiving.GameMessageReceiver;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.socketrpc.SocketServer;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Connector implements IConnecterForSetup {

    private ArrayList<IConnectorObserver> observers;

    @Inject
    private NodeInfoList nodeInfoList;

    @Inject
    private FaultDetector faultDetector;

    @Inject
    private GameMessageClient gameMessageClient;

    @Inject
    private static Logger logger;//NOSONAR

    @Inject
    private SocketServer socketServer;

    @Inject
    private MessageDirector messageDirector;

    @Inject
    private GameMessageReceiver gameMessageReceiver;

    private IFinder finder;


    public Connector() {
    //Inject
    }

    public void start(){
        observers = new ArrayList<>();
        finder = new RoomFinder();

        faultDetector.setObservers(observers);
        gameMessageReceiver.setObservers(observers);

        messageDirector.setGameMessageReceiver(gameMessageReceiver);
        messageDirector.setFaultDetectionMessageReceiver(faultDetector.getFaultDetectionMessageReceiver());

        socketServer.setServerObserver(messageDirector);
        socketServer.startThread();
    }

    public Connector(FaultDetector faultDetector, GameMessageClient gameMessageClient, RoomFinder finder, SocketServer socketServer) {
        observers = new ArrayList<>();
        nodeInfoList = new NodeInfoList();
        this.finder = finder;
        this.gameMessageClient = gameMessageClient;
        this.faultDetector = faultDetector;

        faultDetector.setObservers(observers);

        GameMessageReceiver gameMessageReceiver1 = new GameMessageReceiver();
        gameMessageReceiver1.setObservers(observers);

        MessageDirector messageDirector1 = new MessageDirector();
        messageDirector1.setFaultDetectionMessageReceiver(faultDetector.getFaultDetectionMessageReceiver());
        messageDirector1.setGameMessageReceiver(gameMessageReceiver1);

        socketServer.setServerObserver(messageDirector1);
        socketServer.startThread();
    }

//    //TODO replace with GUICE, inject singleton
//    public static Connector getInstance() {
//        if (instance == null) {
//            instance = new Connector();
//        }
//        return instance;
//    }

    public List<String> getAvailableRooms() {
        try {
            return finder.getAvailableRooms();
        } catch (DiscoveryException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public RoomModel createRoom(String roomName, String ip, String password) {
        try {
            RoomModel createdRoom = finder.createGameRoomModel(roomName, ip, password);
            nodeInfoList.add(new NodeInfo(ip, true, true));
            return createdRoom;
        } catch (DiscoveryException e) {
            logger.log(Level.INFO, e.getMessage());
        }
        return null;
    }

    public RoomModel joinRoom(String roomName, String ip, String password) {
        try {
            RoomModel joinedRoom = finder.joinGameRoomModel(roomName, ip, password);
            if (makeConnection(joinedRoom.getLeaderIP())) {
                addLeaderToNodeInfoList(joinedRoom.getLeaderIP());
                setJoiner();
                return joinedRoom;
            }
        } catch (DiscoveryException e) {
            logger.log(Level.INFO, e.getMessage(), e);
        }
        return null;
    }

    public RoomModel updateRoom(RoomModel room) {
        try {
            return finder.getRoom(room);
        } catch (DiscoveryException e) {
            logger.log(Level.INFO, e.getMessage());
        }
        return null;
    }

    public void startRoom(RoomModel room) {
        try {
            for (String hostIP : room.getHosts()) {
                nodeInfoList.add(new NodeInfo(hostIP, true, false));
            }
            finder.startGameRoom(room.getRoomName());
            setLeader();
        } catch (DiscoveryException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void addObserver(IConnectorObserver observer) {
        observers.add(observer);
    }

    public void setLeader() {
        faultDetector.setLeader(nodeInfoList);
    }

    public void setJoiner() {
        faultDetector.setPlayer(nodeInfoList);
    }

    public boolean makeConnection(String destinationIP) {
        try {
            new FaultDetectionClient().makeConnection(destinationIP);
            return true;
        } catch (NodeCantBeReachedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return false;
    }

    public void addToNodeInfoList(String txtIP) {
        nodeInfoList.addIp(txtIP);
    }

    public void addLeaderToNodeInfoList(String txtIp) {
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setLeader(true);
        nodeInfo.setIp(txtIp);
        nodeInfoList.add(nodeInfo);
    }

    public boolean sendTurn(Round turn) {
        //TODO get real leaderIP for this function
        return gameMessageClient.sendTurnModel("leader ip", turn);
    }

    public void updateAllPeers(Round roundModel) {
        List<String> ips = nodeInfoList.getAllIps();
        gameMessageClient.sendRoundToAllPlayers(ips.toArray(new String[0]), roundModel);
    }

    public void addIP(String text) {
        nodeInfoList.addIp(text);
    }

    public NodeInfoList getIps() {
        return nodeInfoList;
    }

    public void setNodeInfoList(NodeInfoList nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
    }
}