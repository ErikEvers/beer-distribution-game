package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.Global;
import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for making for checking if all the nodes are still connected to the game.
 * It sends a 'PingMessage' every interval to every node that is in the 'NodeInfoList' and where the isConnected
 * attribute of the node equals true. It also keeps track of the amount of times it was unable to reach a specific node.
 * If that amount exceeds a certain limit this class sends a 'FaultMessage' to every node it is able to reach.
 * If the nodes respond that they can't reach the specific node, the isConnected attribute of that node is set to false.
 *
 * @author Oscar, Tarik
 * @see org.han.ica.asd.c.faultdetection.messagetypes.PingMessage
 * @see NodeInfoList
 * @see FaultMessage
 * @see FaultDetectorPlayer
 */
public class FaultDetectorLeader extends TimerTask {
    @Inject
    private NodeInfoList nodeInfoList;

    @Inject
    private FaultDetectionClient faultDetectionClient;

    @Inject
    private FaultHandlerLeader faultHandlerLeader;

    @Inject
    private FailLog failLog;

    @Inject
    private static Logger logger;

    private List<String> ips;

    private Timer timer;

    private List<IConnectorObserver> observers;

    public FaultDetectorLeader() {
        //for inject purposes
    }

    /**
     * Makes a connection with a node every interval and sends 'FaultMessages' to every node that was reached,
     * when a specific node can't be reached. It also checks if this machine, that is running the class, can reach
     * anyone but himself. If he can't reach anyone he stops trying to connect to the nodes in the 'NodeInfoList'.
     *
     * @author Oscar, Tarik
     * @see FaultMessage
     * @see NodeInfoList
     */
    @Override
    public void run() {
        //TODO remove the printlns.
        //Tries to make the connection once every set interval.
        ips = nodeInfoList.getActiveIps();
        for (String ip : ips) {
            logger.log(Level.INFO, "Sending Ping to : {0} : {1}", new Object[]{ip, new Date()});
            makeConnection(ip);
            logger.log(Level.INFO,"Ping Sent: {0}", new Date());
        }

        //Checks if node wasn't reached 3 times, it then sends a faultMessage to all peers that can be reached.
        sendFaultMessagesToActivePlayers(ips);

        checkIfThisMachineIsDisconnected();
    }

    /**
     * Starts the Timertask that calls the run() method every set interval.
     * It calls the createTimer method to create a timer. It then schedules the fixed rate at which the task needs
     * to be run.
     *
     * @author Oscar, Tarik
     * @see TimerTask
     * @see Timer
     */
    public void start() {
        //running timer task as daemon thread
        timer = createTimer(true);
        timer.scheduleAtFixedRate(this, 0, Global.FaultDetectionInterval);
    }

    /**
     * Creates the 'Timer' that calls the 'run' method every set interval.
     *
     * @param isDeamon The boolean value to set isDeamon to.
     * @return The 'Timer' that is used in the 'start' method.
     * @author Oscar
     * @see Timer
     */
    public Timer createTimer(Boolean isDeamon) {
        return new Timer(isDeamon);
    }

    /**
     * Calls the 'sendFaultMessage' method for every ip in the List of ips.
     * It also checks that the ip to send to is active and is not yet failed.
     *
     * @param ips The list of ip's that are currently connected.
     * @author Oscar, Tarik
     */
    public void sendFaultMessagesToActivePlayers(List<String> ips) {
        for (String ip : ips) {
            if (failLog.checkIfIpIsFailed(ip)) {
                sendFaultMessage(ip);
            }
        }
    }

    /**
     * Checks if the machine can reach anyone but himself.
     * If the machine can't reach anyone but himself he calls the iAmDisconnected method at the 'FaultHandlerLeader'
     *
     * @author Oscar
     * @see FaultHandlerLeader
     */
    public void checkIfThisMachineIsDisconnected() {
        if (failLog.getSuccessSize() <= 1) {
            //If the Leader can only reach himself and noone else, he is probably disconnected so no longer leader.
            faultHandlerLeader.iAmDisconnected();
            timer.cancel();
            timer.purge();
        }
    }

    /**
     * Checks the attribute isAlive of the 'FaultMessageResponse' it received.
     * If isAlive equals false, the method incrementFailure gets called at the 'FaultHandlerLeader'.
     *
     * @param faultMessageResponse The 'FaultMessageReponse' that was received.
     * @author Tarik
     * @see FaultMessageResponse
     * @see FaultHandlerLeader
     */
    public void faultMessageResponseReceived(FaultMessageResponse faultMessageResponse) {
        Boolean isAlive = faultMessageResponse.getAlive();
        String ip = faultMessageResponse.getIpOfSubject();

        if (!isAlive) {
            faultHandlerLeader.incrementFailure(ip);
        }
    }

    /**
     * Calls the makeConnection method on the 'FaultDetectionClient'.
     * If an exception occurs while the 'FaultDetectionClient' runs the makeConnection method, this method will call the
     * increment method on the 'FailLog'.
     * If the connection doesnt throw an exception, the amount of fails is reset on the 'FailLog'.
     * It then resets the amount of unsuccessful connections per peer on the 'FaultHandlerLeader'.
     * It then updates the 'NodeInfoList' and sets the value of isConnected to true on the specific node.
     *
     * @param ip The ip to make a connection with.
     * @author Oscar, Tarik
     * @see FaultDetectionClient
     * @see FailLog
     * @see FaultHandlerLeader
     * @see NodeInfoList
     */
    private void makeConnection(String ip) {
        try {
            faultDetectionClient.makeConnection(ip);
            failLog.reset(ip);
            faultHandlerLeader.reset(ip);
            nodeInfoList.updateIsConnected(ip, true);
        } catch (NodeCantBeReachedException e) {
            if (!failLog.checkIfIpIsFailed(ip)) {
                failLog.increment(ip);
            }
        }
    }

    /**
     * Sends a 'FaultMessage' to the specified ip.
     * It sends a 'FaultMessage' to every ip that is currently known to be connected to the game. Aka all the nodes
     * in the 'NodeInfoList' that have the attribute isConnected set to true.
     *
     * @param failingIp The ip of the node that cant't reached by the current machine.
     * @author Oscar, Tarik
     * @see FaultMessage
     * @see NodeInfoList
     */
    private void sendFaultMessage(String failingIp) {
        for (String ip : ips) {
            if (failLog.isAlive(ip)) {
                faultDetectionClient.sendFaultMessage(new FaultMessage(failingIp), ip);
            }
        }
    }

    /**
     * Sets new faultHandlerLeader.
     *
     * @param faultHandlerLeader New value of faultHandlerLeader.
     */
    public void setFaultHandlerLeader(FaultHandlerLeader faultHandlerLeader) {
        this.faultHandlerLeader = faultHandlerLeader;
    }

    /**
     * Sets new failLog.
     *
     * @param failLog New value of failLog.
     */
    public void setFailLog(FailLog failLog) {
        this.failLog = failLog;
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
     * Sets new ips.
     *
     * @param ips New value of ips.
     */
    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    void setTimer(Timer t) {
        this.timer = t;
    }

    public void setObservers(List<IConnectorObserver> observers) {
        this.observers = observers;
    }

    public void setNodeInfoList(NodeInfoList nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
        failLog.setNodeInfoList(nodeInfoList);
    }


}
