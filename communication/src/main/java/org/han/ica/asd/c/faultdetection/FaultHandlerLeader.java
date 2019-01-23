package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.IPlayerDisconnectedObserver;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class will call methods on external interfaces when needed. Example: a node is disconnected from the game, this
 * class will then call the 'AgentComponent' to make sure an agent is started instead of the disconnected node.
 * It also keeps track of the amount of nodes that are able to reach nodes that this machine can't reach.
 *
 * @author Oscar, Tarik
 */
public class FaultHandlerLeader {

    private NodeInfoList nodeInfoList;

    private HashMap<String, Integer> amountOfFailsPerIp;
    private boolean iAmDisconnected;
    private List<IConnectorObserver> observers;

    private static Logger logger = Logger.getLogger(FaultHandlerLeader.class.getName());

    FaultHandlerLeader() {
        amountOfFailsPerIp = new HashMap<>();
        iAmDisconnected = false;
    }

    /**
     * Increments the amount of fails per node. This means it keeps track per node how many nodes can reach said node.
     * It also updates the status of the node when more than half the nodes can't reach said node.
     *
     * @param ip The ip of the node that can/can't be reached.
     * @return The ip when its incremented, else it returns null
     * @author Oscar
     */
    public String incrementFailure(String ip) {
        increment(ip);

        if (amountOfFailsPerIp.get(ip) > 0) {

            // Dit is om niet meer te pingen tot er een rejoin request gedaan word.
            // Dan moet het dus weer op true gezet worden
            nodeInfoList.updateIsConnected(ip, false);

            logger.log(Level.INFO, "Speler met ip : {0} is uitgevallen", new Object[]{ip});

            notifyObserversPlayerDied(nodeInfoList.getIdByIp(ip));
            return ip;
        } else {
            //TODO Call Relay system here when implemented.
            return null;
        }
    }

    /**
     * Gets value of iAmDisconnected.
     *
     * @return The value of iAmDisconnected
     */
    public boolean isLeaderAlive() {
        return iAmDisconnected;
    }

    /**
     * Resets the amount of failures for a specific node, given the ip of said node.
     *
     * @param ip The ip of the node.
     * @author Oscar
     */
    public void reset(String ip) {
        putIpInListIfNotAlready(ip);
        amountOfFailsPerIp.put(ip, 0);
    }

    /**
     * Sets the value of iAmDisconnected to true
     *
     * @author Oscar
     */
    public void iAmDisconnected() {
        if(!iAmDisconnected) {
            iAmDisconnected = true;
            logger.log(Level.INFO, "Deze machine kan niemand bereiken");
            notifyObserversIDied();
        }
    }

    /**
     * Notifies every observer that this machine can't connect to anyone.
     *
     * @author Tarik
     */
    private void notifyObserversIDied() {
        if (observers != null && !observers.isEmpty()) {
            for (IConnectorObserver observer : observers) {
                if (observer instanceof IPlayerDisconnectedObserver) {
                    ((IPlayerDisconnectedObserver) observer).iAmDisconnected();
                }
            }
        }
    }

    /**
     * Notifies every observer that a node can't be reached and is disconnected.
     *
     * @param ip The ip that was not reached.
     * @author Tarik
     */
    private void notifyObserversPlayerDied(String playerId) {
        if (!observers.isEmpty()) {
            for (IConnectorObserver observer : observers) {
                if (observer instanceof IPlayerDisconnectedObserver) {
                    ((IPlayerDisconnectedObserver) observer).playerIsDisconnected(playerId);
                }
            }
        }
    }

    /**
     * Increments the amount of fails of a specific ip
     *
     * @param ip The ip of which the amount of failures needs to be incremented.
     * @author Oscar
     */
    private void increment(String ip) {
        //Increments the value that is linked to the ip.
        putIpInListIfNotAlready(ip);
        amountOfFailsPerIp.put(ip, amountOfFailsPerIp.get(ip) + 1);
    }

    /**
     * Adds the ip to the list if its not already in the list.
     *
     * @param ip The ip to add to the list.
     * @author Oscar
     */
    private void putIpInListIfNotAlready(String ip) {
        //Puts the ip in the hashmap if it is not yet in that list
        if (!amountOfFailsPerIp.containsKey(ip)) {
            add(ip);
        }
    }

    /**
     * Adds the specified ip to the list.
     *
     * @param ip The ip to add to the list.
     * @author Oscar
     */
    private void add(String ip) {
        //Adds the ip to the list
        amountOfFailsPerIp.put(ip, 0);
    }

    public void setObservers(List<IConnectorObserver> observers) {
        this.observers = observers;
    }

    public void setNodeInfoList(NodeInfoList nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
    }

    /**
     * Sets new amountOfFailsPerIp.
     *
     * @param amountOfFailsPerIp New value of amountOfFailsPerIp.
     */
    public void setAmountOfFailsPerIp(HashMap<String, Integer> amountOfFailsPerIp) {
        this.amountOfFailsPerIp = amountOfFailsPerIp;
    }

    /**
     * Sets new logger.
     *
     * @param logger New value of logger.
     */
    public void setLogger(Logger logger) {
        FaultHandlerLeader.logger = logger;
    }

    /**
     * Gets amountOfFailsPerIp.
     *
     * @return Value of amountOfFailsPerIp.
     */
    public HashMap<String, Integer> getAmountOfFailsPerIp() {
        return amountOfFailsPerIp;
    }
}
