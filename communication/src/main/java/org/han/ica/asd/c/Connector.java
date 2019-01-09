package org.han.ica.asd.c;

import org.han.ica.asd.c.discovery.IFinder;
import org.han.ica.asd.c.discovery.RoomFinder;
import org.han.ica.asd.c.exceptions.communication.DiscoveryException;
import org.han.ica.asd.c.exceptions.communication.RoomException;
import org.han.ica.asd.c.faultdetection.FaultDetectionClient;
import org.han.ica.asd.c.faultdetection.FaultDetector;
import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfo;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.messagehandler.receiving.GameMessageReceiver;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.model.domain_objects.Configuration;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.RoomModel;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.socketrpc.SocketServer;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Connector implements IConnectorForSetup {
    private static Connector instance = null;

    private ArrayList<IConnectorObserver> observers;

    @Inject
    private NodeInfoList nodeInfoList;

    @Inject
    private FaultDetector faultDetector;

    @Inject
    private GameMessageClient gameMessageClient;
    private String externalIP;

    @Inject
    private static Logger logger;//NOSONAR

    @Inject
    private SocketServer socketServer;

    @Inject
    private MessageDirector messageDirector;

    @Inject
    private GameMessageReceiver gameMessageReceiver;

    @Inject
    private IFinder finder;


    public Connector() {
        //Inject
    }

    public void start() {
        observers = new ArrayList<>();
        finder = new RoomFinder();

        gameMessageClient = new GameMessageClient();
        faultDetector = new FaultDetector();
        faultDetector.setObservers(observers);

        externalIP = getExternalIP();

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

    public List<String> getAvailableRooms() {
        try {
            return finder.getAvailableRooms();
        } catch (DiscoveryException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return new ArrayList<>();
    }


    public RoomModel createRoom(String roomName, String password) {
        try {
            RoomModel createdRoom = finder.createGameRoomModel(roomName, externalIP, password);
            nodeInfoList.add(new NodeInfo(externalIP, true, true));
            return createdRoom;
        } catch (DiscoveryException e) {
            logger.log(Level.INFO, e.getMessage());
        }
        return null;
    }

    public RoomModel joinRoom(String roomName,  String password) throws RoomException, DiscoveryException {
        RoomModel joinedRoom = finder.joinGameRoomModel(roomName, externalIP, password);
        if (makeConnection(joinedRoom.getLeaderIP())) {
            addLeaderToNodeInfoList(joinedRoom.getLeaderIP());
            setJoiner();
        }
        return joinedRoom;
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

    public void removeHostFromRoom(RoomModel room, String hostIP) {
        try {
            finder.removeHostFromRoom(room, hostIP);
        } catch (DiscoveryException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void removeYourselfFromRoom(RoomModel room) {
        try {
            finder.removeHostFromRoom(room, externalIP);
        } catch (DiscoveryException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void chooseFacility(Facility facility) {
        //for later use
    }

    @Override
    public List<Facility> getAllFacilities() {
        return new ArrayList<Facility>();
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

    public void sendConfiguration(Configuration configuration) {
        List<String> ips = nodeInfoList.getAllIps();
        gameMessageClient.sendConfigurationToAllPlayers(ips.toArray(new String[0]), configuration);
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

    public String getExternalIP() {
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        String ip = null;
        try {
            ip = in.readLine();
            in.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return ip;
    }
}