package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.Global;
import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is used by a player that's not a leader. It detect a failing connection and figuring out the failing node.
 * This class is responsible for making for checking if all the nodes are still connected to the game.
 * It receives a 'PingMessage' from the leader. It also keeps track of the last received message time. And checks every
 * interval whether the leader did send a message recently or not. If that time difference exceeds a certain limit the
 * fault is recognized and the handling starts.
 *
 * @author Oscar, Tarik
 */
public class FaultDetectorPlayer extends TimerTask {
    @Inject
    private FaultDetectionClient faultDetectionClient;
    @Inject
    private FaultHandlerPlayer faultHandlerPlayer;

    private NodeInfoList nodeInfoList;

    private static final long FIVE_MINUTES = 300000;
    private long lastReceived;
    private boolean leaderWasPinging;
    private Timer timer;
    private List<IConnectorObserver> observers;
    private HashMap<String, Long> playersWhoAlreadyCouldNotReachLeader;
    private boolean active;

    FaultDetectorPlayer() {
        //for inject purposes
    }

    /**
     * Checks if leader didn't ping for a long time and asks other players whether they can connect with the leader.
     * After the players respond or timeout it decides who died.
     * It also checks if the leaderWasPinging variable is already set to false so it won't run it twice.
     *
     * @author Tarik
     */
    public void run() {
        if (leaderWasPinging && leaderIsNotPinging()) {
            leaderWasPinging = false;
            faultHandlerPlayer.reset();
            askOtherPlayers();
            faultHandlerPlayer.whoIsDead();
        }
    }

    /**
     * Starts the Timertask that calls the run() method every set interval.
     * It calls the createTimer method to create a timer. It then schedules the fixed rate at which the task needs
     * to be run.
     *
     * @author Oscar, Tarik
     */

    public void start() {
        lastReceived = System.currentTimeMillis();
        leaderWasPinging = true;
        playersWhoAlreadyCouldNotReachLeader = new HashMap<>();
        timer = createTimer(true);
        timer.scheduleAtFixedRate(this, 0, Global.FAULT_DETECTION_INTERVAL);
        faultHandlerPlayer.setObservers(observers);
        active = true;
    }

    /**
     * Stops the Timertask and sets the fault detector to false so the messages wont be handled.
     *
     * @author Tarik
     * @see TimerTask
     * @see Timer
     */
    public void stop(){
        timer.cancel();
        this.active = false;
    }

    /**
     * Returns the 'FaultDetectorPlayer' state.
     *
     * @return isActive
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Creates the 'Timer' that calls the 'run' method every set interval.
     *
     * @param isDeamon The boolean value to set isDeamon to.
     * @return The 'Timer' that is used in the 'start' method.
     * @author Oscar
     */
    public Timer createTimer(Boolean isDeamon) {
        return new Timer(isDeamon);
    }

    /**
     * Checks if leader didn't ping for at least 3 times the FaultDetectionInterval
     *
     * @return true if leader didn't ping and false if he did.
     * @author Tarik
     */
    public boolean leaderIsNotPinging() {
        long current = System.currentTimeMillis();
        long timeDifference = current - lastReceived;
        long threeIntervals = Global.FAULT_DETECTION_INTERVAL * 3L;

        return timeDifference > threeIntervals;
    }

    /**
     * This method checks whether a given timestamp is within five minutes of the current time.
     *
     * @param lastTime the timestamp in milliseconds
     * @return true if timestamp is within five minutes and false if its not.
     * @author Tarik
     */
    private boolean timestampIsRecentlyReceived(long lastTime) {
        long current = System.currentTimeMillis();
        long timeDifference = current - lastTime;

        return timeDifference < FIVE_MINUTES;
    }

    /**
     * This method sends a 'CanYouReachLeaderMessage' to players from which i didn't already received the same message.
     * It waits for the responses and increments/sets the values in the 'FaultHandlerPlayer' so it can decide who is disconnected.
     *
     * @author Tarik
     */
    private void askOtherPlayers() {
        List<String> ips = nodeInfoList.getActiveIpsWithoutLeader();
        faultHandlerPlayer.setAmountOfActiveIps(ips.size()-1);
        HashMap<String, Long> filter = new HashMap<>(playersWhoAlreadyCouldNotReachLeader);

        // filters out IPs from ips that already send a message within the past 5 minutes
        ips.removeIf(ip -> ip == nodeInfoList.getMyIp() || filter.containsKey(ip) && timestampIsRecentlyReceived(filter.get(ip)));

        faultHandlerPlayer.setFilteredAmount(filter.size());
        Map<String, Object> response = faultDetectionClient.sendCanYouReachLeaderMessageToAll(ips.toArray(new String[0]), new CanYouReachLeaderMessage());

        for (Object responseMessage : response.values()) {
            if (responseMessage instanceof Exception) {
                faultHandlerPlayer.incrementAmountOfFailingIps();
            } else if (responseMessage instanceof CanYouReachLeaderMessage) {
                CanYouReachLeaderMessage canYouReachLeaderMessage = (CanYouReachLeaderMessage) responseMessage;
                if (canYouReachLeaderMessage.getLeaderState()) {
                    faultHandlerPlayer.incrementAmountOfConnectionsWithLeader();
                }
            }
        }
    }

    /**
     * This method returns a response to the 'CanYouReachLeaderMessage' it received with the same message after setting
     * the 'leaderState'.
     * It also updates the list with the senderIp which needs to be used as a filter when 'askOtherPlayer()' is called.
     *
     * @param canYouReachLeaderMessage The message that need to be returned after setting data.
     * @param senderIp                 This ip is used to identify the sender.
     * @return the object after being filled as response.
     * @author Tarik
     */
    public Object canYouReachLeaderMessageReceived(CanYouReachLeaderMessage canYouReachLeaderMessage, String senderIp) {
        boolean leaderIsAlive;
        // put senderIp in local list with currentTime
        playersWhoAlreadyCouldNotReachLeader.put(senderIp, System.currentTimeMillis());

        String leaderIp = nodeInfoList.getLeaderIp();

        if (leaderIsNotPinging()) {
            leaderIsAlive = false;
        } else {
            try {
                if (leaderIp == null) {
                    leaderIsAlive = false;
                } else {
                    faultDetectionClient.makeConnection(leaderIp);
                    leaderIsAlive = true;
                }
            } catch (NodeCantBeReachedException e) {
                leaderIsAlive = false;
            }
        }
        canYouReachLeaderMessage.setLeaderState(leaderIsAlive);

        return canYouReachLeaderMessage;
    }

    /**
     * Sets the lastReceived timestamp in millisecond when a 'PingMessage' is received.
     *
     * @param pingMessage this message is not used but could be used in the future.
     * @author Tarik
     */
    public void pingMessageReceived(PingMessage pingMessage) {
        lastReceived = System.currentTimeMillis();
        System.out.println("ping received : " + lastReceived);
        leaderWasPinging = true;
    }

    /**
     * Sets new faultDetectionClient.
     *
     * @param faultDetectionClient New value of faultDetectionClient.
     */
    public void setFaultDetectionClient(FaultDetectionClient faultDetectionClient) {
        this.faultDetectionClient = faultDetectionClient;
    }

    /**
     * Sets new leaderWasPinging.
     *
     * @param leaderWasPinging New value of leaderWasPinging.
     */
    public void setLeaderWasPinging(boolean leaderWasPinging) {
        this.leaderWasPinging = leaderWasPinging;
    }

    /**
     * Sets new lastReceived.
     *
     * @param lastReceived New value of lastReceived.
     */
    public void setLastReceived(long lastReceived) {
        this.lastReceived = lastReceived;
    }

    /**
     * Sets new nodeInfoList.
     *
     * @param nodeInfoList New value of nodeInfoList.
     */
    public void setNodeInfoList(NodeInfoList nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
        faultHandlerPlayer.setNodeInfoList(nodeInfoList);
    }

    /**
     * Sets new faultHandlerPlayer.
     *
     * @param faultHandlerPlayer New value of faultHandlerPlayer.
     */
    public void setFaultHandlerPlayer(FaultHandlerPlayer faultHandlerPlayer) {
        this.faultHandlerPlayer = faultHandlerPlayer;
    }

    /**
     * Sets new playersWhoAlreadyCouldNotReachLeader.
     *
     * @param playersWhoAlreadyCouldNotReachLeader New value of playersWhoAlreadyCouldNotReachLeader.
     */
    public void setPlayersWhoAlreadyCouldNotReachLeader(HashMap<String, Long> playersWhoAlreadyCouldNotReachLeader) {
        this.playersWhoAlreadyCouldNotReachLeader = playersWhoAlreadyCouldNotReachLeader;
    }

    /**
     * Sets new observers.
     *
     * @param observers New value of observers.
     */
    public void setObservers(List<IConnectorObserver> observers) {
        this.observers = observers;
    }

    /**
     * Gets nodeInfoList.
     *
     * @return Value of nodeInfoList.
     */
    public NodeInfoList getNodeInfoList() {
        return nodeInfoList;
    }

    /**
     * Gets leaderWasPinging.
     *
     * @return Value of leaderWasPinging.
     */
    boolean getLeaderWasPinging() {
        return leaderWasPinging;
    }

    /**
     * Gets lastReceived.
     *
     * @return Value of lastReceived.
     */
    long getLastReceived() {
        return lastReceived;
    }

    /**
     * Gets timer.
     *
     * @return Value of timer.
     */
    public Timer getTimer() {
        return timer;
    }

}
