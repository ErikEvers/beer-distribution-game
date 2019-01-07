package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;
import org.han.ica.asd.c.interfaces.communication.IPlayerDisconnectedObserver;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class will call methods on external interfaces when needed. Example: a node is disconnected from the game, this
 * class will then call the 'AgentComponent' to make sure an agent is started instead of the disconnected node.
 * It also keeps track of the amount of nodes that are able to reach nodes that this machine can't reach.
 */
public class FaultHandlerLeader {
    private HashMap<String, Integer> amountOfFailsPerIp;
    private NodeInfoList nodeInfoList;
    private boolean iAmDisconnected;
    private ArrayList<IConnectorObserver> observers;

    HashMap<String, Integer> getAmountOfFailsPerIp() {
        return amountOfFailsPerIp;
    }

    FaultHandlerLeader(NodeInfoList nodeInfoList, ArrayList<IConnectorObserver> observers) {
        this.observers = observers;
        amountOfFailsPerIp = new HashMap<>();
        this.nodeInfoList = nodeInfoList;
        iAmDisconnected = false;
    }

    /**
     * Increments the amount of fails per node. This means it keeps track per node how many nodes can reach said node.
     * It also updates the status of the node when more than half the nodes can't reach said node.
     *
     * @param ip The ip of the node that can/can't be reached.
     * @return The ip when its incremented, else it returns null
     * @author Oscar, Tarik
     */
    public String incrementFailure(String ip) {
        increment(ip);

        if (amountOfFailsPerIp.get(ip).equals(nodeInfoList.size())) {

            nodeInfoList.updateIsConnected(ip, false);

            notifyObserversPlayerDied(ip);

            return ip;
        }
        return null;
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
     * Retrieves the value of isConnected of a specific ipAddress
     *
     * @param ip The ip of which the status is requested.
     * @return The value of isConnected for the specified ipAddress.
     * @author Oscar, Tarik
     */
    public boolean isPeerAlive(String ip) {
        //TODO probably remove this method entirely
        return nodeInfoList.getStatusOfOneNode(ip);
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
        //Can be called when the leader cant reach anyone but himself
        iAmDisconnected = true;

        notifyObserversIDied();
    }

    /**
     * Notifies every observer that this machine can't connect to anyone.
     *
     * @author Tarik
     */
    private void notifyObserversIDied() {
        if (!observers.isEmpty()) {
            for (IConnectorObserver observer : observers) {
                if (observer instanceof IPlayerDisconnectedObserver) {
                    ((IPlayerDisconnectedObserver) observer).iAmDisconnected();
                }
            }
        }
    }


    /**
     * Notifies every observer that a node can't be reached and is disconnected.
     * @param ip The ip that was not reached.
     * @author Tarik
     */
    private void notifyObserversPlayerDied(String ip) {
        if (!observers.isEmpty()) {
            for (IConnectorObserver observer : observers) {
                if (observer instanceof IPlayerDisconnectedObserver) {
                    ((IPlayerDisconnectedObserver) observer).playerIsDisconnected(ip);
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
}
