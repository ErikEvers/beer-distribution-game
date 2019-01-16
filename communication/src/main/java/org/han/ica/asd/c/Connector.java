package org.han.ica.asd.c;

import org.han.ica.asd.c.discovery.RoomFinder;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.exceptions.communication.SendGameMessageException;
import org.han.ica.asd.c.model.domain_objects.Facility;
import org.han.ica.asd.c.model.domain_objects.Leader;
import org.han.ica.asd.c.model.domain_objects.Player;
import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.interfaces.gameleader.IConnectorForLeader;
import org.han.ica.asd.c.exceptions.communication.TransactionException;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;
import org.han.ica.asd.c.model.domain_objects.RoomModel;
import org.han.ica.asd.c.exceptions.communication.DiscoveryException;
import org.han.ica.asd.c.exceptions.communication.RoomException;
import org.han.ica.asd.c.faultdetection.FaultDetectionClient;
import org.han.ica.asd.c.faultdetection.FaultDetector;
import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorForSetup;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.IFinder;
import org.han.ica.asd.c.interfaces.gamelogic.IConnectedForPlayer;
import org.han.ica.asd.c.messagehandler.receiving.GameMessageReceiver;
import org.han.ica.asd.c.messagehandler.sending.GameMessageClient;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.han.ica.asd.c.socketrpc.SocketServer;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class Connector implements IConnectorForSetup, IConnectedForPlayer, IConnectorForLeader {
    private static Connector instance = null;
    private static String leaderIp = null;

    private static ArrayList<IConnectorObserver> observers;

    @Inject
    private IGameStore persistence;

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

    @Inject
    private IFinder finder;

    private String externalIP;
    public static String internalIP;


    Provider<GameLeader> gameLeaderProvider;

    @Inject
    public Connector(Provider<GameLeader> gameLeaderProvider) {
        this.gameLeaderProvider = gameLeaderProvider;

    }

    public void start() {
        observers = new ArrayList<>();

        faultDetector.setObservers(observers);
        try {
            externalIP = getExternalIP();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        internalIP = getInternalIP();

        faultDetector.setObservers(observers);
        GameMessageReceiver.setObservers(observers);

        messageDirector.setGameMessageReceiver(gameMessageReceiver);
        messageDirector.setFaultDetectionMessageReceiver(faultDetector.getFaultDetectionMessageReceiver());

        socketServer.setServerObserver(messageDirector);
        socketServer.startThread();
    }

    public void start(String ip){
        start();
        internalIP = ip;
    }


    public List<String> getAvailableRooms() {
        try {
            return finder.getAvailableRooms();
        } catch (DiscoveryException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return new ArrayList<>();
    }


    public RoomModel createRoom(String roomName, String password, BeerGame beerGame) {
        try {
            RoomModel createdRoom = finder.createGameRoomModel(roomName, internalIP, password);
            GameLeader leader = gameLeaderProvider.get();
            leader.init(internalIP, createdRoom, beerGame);

            return createdRoom;
        } catch (DiscoveryException e) {
            logger.log(Level.INFO, e.getMessage());
        }
        return null;
    }

    public RoomModel joinRoom(String roomName, String password) throws RoomException, DiscoveryException {
        RoomModel r = new RoomModel();
        r.setRoomName(roomName);
        if (makeConnection(finder.getRoom(r).getLeaderIP())) {
            RoomModel joinedRoom = finder.joinGameRoomModel(roomName, internalIP, password);
            Connector.leaderIp = joinedRoom.getLeaderIP();
            return  joinedRoom;
        } else {
            throw new DiscoveryException("Can't connect to leader");
        }
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
            finder.startGameRoom(room.getRoomName());
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
    public void chooseFacility(Facility facility, String playerId) throws FacilityNotAvailableException, SendGameMessageException {
        gameMessageClient.sendChooseFacilityMessage(leaderIp, facility, playerId);
    }

    @Override
    public GamePlayerId getGameData(String userName) throws SendGameMessageException {
        return gameMessageClient.sendGameDataRequestMessage(leaderIp, userName);
    }

    @Override
    public void removeYourselfFromRoom(RoomModel room) {
        try {
            finder.removeHostFromRoom(room, internalIP);
            Connector.leaderIp = null;
        } catch (DiscoveryException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void addObserver(IConnectorObserver observer) {
        observers.add(observer);
        faultDetector.setObservers(observers);
        GameMessageReceiver.setObservers(observers);
    }

    /**
     * The data of a specific round gets sent to the participants of said game.
     *
     * @param previousRound
     * @param newRound
     */
    @Override
    public void sendRoundDataToAllPlayers(Round previousRound, Round newRound, BeerGame beerGame) throws TransactionException {
        //initNodeInfoList();
        List<String> ips = beerGame.getPlayers().stream().map(Player::getIpAddress).collect(Collectors.toList());

        gameMessageClient.sendRoundToAllPlayers(ips.toArray(new String[0]), previousRound, newRound);
    }

    public void initNodeInfoList() {
        List<Player> playerList = persistence.getGameLog().getPlayers();
        Leader leader = persistence.getGameLog().getLeader();
        nodeInfoList.init(playerList, leader);
    }

    public void startFaultDetector() {
        initNodeInfoList();
        Leader leader = persistence.getGameLog().getLeader();

        if (internalIP.equals(leader.getPlayer().getIpAddress())) {
            faultDetector.startFaultDetectorLeader(nodeInfoList);
        } else {
            faultDetector.startFaultDetectorPlayer(nodeInfoList);
        }
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

    public void sendTurn(Round turn) throws SendGameMessageException {
        Leader leader = persistence.getGameLog().getLeader();
        gameMessageClient.sendTurnModel(leader.getPlayer().getIpAddress(), turn);
    }

    @Override
    public void sendGameStart(BeerGame beerGame) throws TransactionException {
        List<String> ips = beerGame.getPlayers().stream().map(Player::getIpAddress).collect(Collectors.toList());
        gameMessageClient.sendStartGameToAllPlayers(ips.toArray(new String[0]), beerGame);
    }

    public NodeInfoList getIps() {
        return nodeInfoList;
    }

    public void setNodeInfoList(NodeInfoList nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
    }

    /**
     * n
     * Gets the external IP of your router.
     *
     * @return The IP.
     */
    private String getExternalIP() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        String ip;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()))) {
            ip = in.readLine();
        }
        return ip;
    }

    /**
     * Gets the local ip4 address from ethernet connection.
     *
     * @return The IP.
     */
    private String getInternalIP() {
        Map<String, String> ips = listAllIPs();


        for (Map.Entry<String, String> interfaceIP : ips.entrySet())
        {
            if(interfaceIP.getKey().contains("eth")){
                return interfaceIP.getValue();
            }
        }

        return null;
    }

    /**
     * Gets the ip4 address of an ethernet interface.
     *
     * @param nets List of network interfaces.
     * @return The IP.
     */
    private String getIpOfInterFace(Enumeration<NetworkInterface> nets) {
        String ip = null;

        if (nets != null) {
            for (NetworkInterface netint : Collections.list(nets)) {
                if (netint.getName().contains("eth")) {
                    for (InetAddress inetAddress : Collections.list(netint.getInetAddresses())) {
                        if (inetAddress instanceof Inet4Address) {
                            ip = inetAddress.getHostAddress();
                        }
                    }
                }
            }
        }
        return ip;
    }

    public Map<String, String> listAllIPs(){
        Map<String, String> ips = new LinkedHashMap<String, String>();
        Enumeration<NetworkInterface> interfaces = getInterfaceEnem();
        if(interfaces == null){
            return null;
        }
        while(interfaces.hasMoreElements()){
            NetworkInterface networkInterface = interfaces.nextElement();
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            if(!checkIfLoopBackAddres(networkInterface)) {
                checkInterfacesAndGiveIP(ips, networkInterface, addresses);
            }
        }
        try {
            ips.put("external", getExternalIP());
        } catch (IOException e) {
            logger.log(Level.INFO, "No external ip available", e);
        }
        return ips;
    }

    @Override
    public void setMyIp(String ip) {
        internalIP = ip;
    }

    private void checkInterfacesAndGiveIP(Map<String, String> ips, NetworkInterface networkInterface, Enumeration<InetAddress> addresses) {
        while (addresses.hasMoreElements()) {
            InetAddress ip = addresses.nextElement();
            if (ip instanceof Inet4Address) {
                String displayName = networkInterface.getDisplayName();
                if(!displayName.contains("Virtual")) {
                    String ipAddress = ip.getHostAddress();
                    ips.put(networkInterface.getName(), ipAddress);
                }

            }
        }
    }

    public boolean checkIfLoopBackAddres(NetworkInterface networkInterface){
        try {
            return networkInterface.isLoopback();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Enumeration<NetworkInterface> getInterfaceEnem() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return interfaces;
    }

    public void setPersistence(IGameStore persistence) {
        this.persistence = persistence;
    }
}