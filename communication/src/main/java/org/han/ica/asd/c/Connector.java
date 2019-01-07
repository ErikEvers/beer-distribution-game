package org.han.ica.asd.c;

import org.han.ica.asd.c.discovery.DiscoveryException;
import org.han.ica.asd.c.discovery.IFinder;
import org.han.ica.asd.c.discovery.RoomException;
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
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.socketrpc.SocketServer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Connector implements IConnecterForSetup {
private static Connector instance = null;
    private ArrayList<IConnectorObserver> observers;
    private NodeInfoList nodeInfoList;
    private FaultDetector faultDetector;
    private IFinder finder;
    private GameMessageClient gameMessageClient;

    private static final Logger LOGGER = Logger.getLogger(Connector.class.getName());

    public Connector() {
        observers = new ArrayList<>();
        nodeInfoList = new NodeInfoList();
        finder = new RoomFinder();
        gameMessageClient = new GameMessageClient();
        faultDetector = new FaultDetector(observers);

        SocketServer socketServer = new SocketServer(new MessageDirector(new GameMessageReceiver(observers), faultDetector.getFaultDetectionMessageReceiver()));
        socketServer.startThread();
    }

    //for tests
    public Connector(FaultDetector faultDetector, GameMessageClient gameMessageClient, RoomFinder finder){
        observers = new ArrayList<>();
        nodeInfoList = new NodeInfoList();
        this.finder = finder;
        this.gameMessageClient = gameMessageClient;
        this.faultDetector = faultDetector;

        SocketServer socketServer = new SocketServer(new MessageDirector(new GameMessageReceiver(observers), faultDetector.getFaultDetectionMessageReceiver()));
        socketServer.startThread();
    }

    //TODO replace with GUICE, inject singleton
    public static Connector getInstance() {
        if (instance == null){
            instance = new Connector();
        }
        return instance;
    }

    public List<String> getAvailableRooms() {
        try {
            return finder.getAvailableRooms();
        } catch (DiscoveryException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public RoomModel createRoom(String roomName, String ip, String password){
        try {
            RoomModel createdRoom = finder.createGameRoomModel(roomName, ip, password);
            nodeInfoList.add(new NodeInfo(ip, true, true));
            return createdRoom;
        } catch (DiscoveryException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
        return null;
    }

    public RoomModel joinRoom(String roomName, String ip, String password){
        try {
            RoomModel joinedRoom = finder.joinGameRoomModel(roomName, ip, password);
            if(makeConnection(joinedRoom.getLeaderIP())){
                addLeaderToNodeInfoList(joinedRoom.getLeaderIP());
                setJoiner();
                return joinedRoom;
            }
        } catch (DiscoveryException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
        return null;
    }

    public RoomModel updateRoom(RoomModel room){
        try {
            return finder.getRoom(room);
        } catch (DiscoveryException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public void startRoom(RoomModel room){
        try {
            for(String hostIP: room.getHosts()){
                nodeInfoList.add(new NodeInfo(hostIP, true, false));
            }
            finder.startGameRoom(room.getRoomName());
            setLeader();
        } catch (DiscoveryException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
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

    public boolean makeConnection(String destinationIP){
        try {
            new FaultDetectionClient().makeConnection(destinationIP);
            return true;
        } catch (NodeCantBeReachedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return false;
    }

    public void addToNodeInfoList(String txtIP) {
        nodeInfoList.addIp(txtIP);
    }
    
    public void addLeaderToNodeInfoList(String txtIp){
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setLeader(true);
        nodeInfo.setIp(txtIp);
        nodeInfoList.add(nodeInfo);
    }

    public void sendTurn(Round turn) {
        //TODO get real leaderIP for this function
        gameMessageClient.sendTurnModel("leader ip", turn);
    }

    public void updateAllPeers(Round roundModel) {
        List<String> ips = nodeInfoList.getAllIps();
        gameMessageClient.sendRoundToAllPlayers(ips.toArray(new String[0]), roundModel);
    }

    public void sendConfiguration(Configuration configuration) {
        List<String> ips = nodeInfoList.getAllIps();
        gameMessageClient.sendConfigurationToAllPlayers(ips, configuration);
    }

    public void addIP(String text) {
        nodeInfoList.addIp(text);
    }

    public NodeInfoList getIps() {
        return nodeInfoList;
    }
}