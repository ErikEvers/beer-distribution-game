package org.han.ica.asd.c;

import org.han.ica.asd.c.discovery.RoomFinder;
import org.han.ica.asd.c.interfaces.persistence.IGameStore;
import org.han.ica.asd.c.model.domain_objects.*;
import org.han.ica.asd.c.exceptions.gameleader.FacilityNotAvailableException;
import org.han.ica.asd.c.gameleader.GameLeader;
import org.han.ica.asd.c.interfaces.gameleader.IConnectorForLeader;
import org.han.ica.asd.c.exceptions.communication.TransactionException;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.GamePlayerId;
import org.han.ica.asd.c.model.domain_objects.RoomModel;
import org.han.ica.asd.c.model.domain_objects.Facility;
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
import org.han.ica.asd.c.socketrpc.SocketServer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
import java.util.List;
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
    private String internalIP;


	Provider<GameLeader> gameLeaderProvider;

    @Inject
    public Connector(Provider<GameLeader> gameLeaderProvider) {
        this.gameLeaderProvider = gameLeaderProvider;
    }

    public void start() {
        observers = new ArrayList<>();
        finder = new RoomFinder();
				nodeInfoList = new NodeInfoList();

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
            RoomModel createdRoom = finder.createGameRoomModel(roomName, externalIP, password);
            GameLeader leader = gameLeaderProvider.get();
            leader.init(externalIP, createdRoom, beerGame);

            return createdRoom;
        } catch (DiscoveryException e) {
            logger.log(Level.INFO, e.getMessage());
        }
        return null;
    }

    public RoomModel joinRoom(String roomName, String password) throws RoomException, DiscoveryException {
        RoomModel joinedRoom = finder.joinGameRoomModel(roomName, externalIP, password);
        if (makeConnection(joinedRoom.getLeaderIP())) {
            Connector.leaderIp = joinedRoom.getLeaderIP();
            return joinedRoom;
        }else{
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
    public void chooseFacility(Facility facility, String playerId) throws FacilityNotAvailableException {
        gameMessageClient.sendChooseFacilityMessage(leaderIp, facility, playerId);
    }

    @Override
    public GamePlayerId getGameData(String userName) throws ClassNotFoundException, IOException {
        return gameMessageClient.sendGameDataRequestMessage(leaderIp, userName);
    }

    @Override
    public void removeYourselfFromRoom(RoomModel room) {
        try {
            finder.removeHostFromRoom(room, externalIP);
            Connector.leaderIp = null;
        } catch (DiscoveryException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public boolean sendTurnData(Round turn) {
        return gameMessageClient.sendTurnModel(leaderIp, turn);
    }

    public void addObserver(IConnectorObserver observer) {
        observers.add(observer);
        faultDetector.setObservers(observers);
        GameMessageReceiver.setObservers(observers);
    }

    @Override
    public void requestFacilityUsage(Facility facility) {
        throw new NotImplementedException();
    }

    @Override
    public List<Facility> getAllFacilities() {
        throw new NotImplementedException();
    }

    @Override
    public void sendSelectedAgent(ProgrammedAgent programmedAgent) {
        throw new NotImplementedException();
    }

    /**
     * The data of a specific round gets sent to the participants of said game.
     * @param previousRound
     * @param newRound
     */
    @Override
    public void sendRoundDataToAllPlayers(Round previousRound, Round newRound, BeerGame beerGame) throws TransactionException {
			//initNodeInfoList();
			List<String> ips = beerGame.getPlayers().stream().map(Player::getIpAddress).collect(Collectors.toList());

			gameMessageClient.sendRoundToAllPlayers(ips.toArray(new String[0]), previousRound, newRound);
    }

    public void startFaultDetector(){
        List<Player> playerList = persistence.getGameLog().getPlayers();
        Leader leader =  persistence.getGameLog().getLeader();
        nodeInfoList.init(playerList,leader);

        if(externalIP.equals(leader.getPlayer().getIpAddress())){
            faultDetector.startFaultDetectorLeader(nodeInfoList);
        }else{
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

    public boolean sendTurn(Round turn) {
        Leader leader =  persistence.getGameLog().getLeader();
        return gameMessageClient.sendTurnModel(leader.getPlayer().getIpAddress(), turn);
    }

    @Override
    public void sendGameStart(BeerGame beerGame) throws TransactionException {
				//initNodeInfoList();
				//List<String> ips = nodeInfoList.getAllIps();
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
        return "25.28.108.244";
    }

    /**
     * Gets the local ip4 address from ethernet connection.
     *
     * @return The IP.
     */
    private String getInternalIP() {
        String ip = null;

        try {
            ip = getIpOfInterFace(NetworkInterface.getNetworkInterfaces());
        } catch (SocketException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return ip;
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

    public void setPersistence(IGameStore persistence) {
        this.persistence = persistence;
    }
}