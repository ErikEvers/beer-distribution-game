package org.han.ica.asd.c.faultdetection;


import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.observers.IConnectorObserver;
import org.han.ica.asd.c.observers.IPlayerDisconnectedObserver;

import java.util.ArrayList;
import java.util.HashMap;

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

    public String incrementFailure(String ip) {
        // Makes sure the ip is in the list.
        // Increments the List by 1.
        //TODO remove println
        increment(ip);

        if (amountOfFailsPerIp.get(ip) >= (nodeInfoList.size() / 2)) {

            nodeInfoList.updateIsConnected(ip, false);

            notifyObserversPlayerDied(ip);
            System.out.println("oh no " + ip + " died");

            return ip;
        }
        return null;
    }

    public boolean isLeaderAlive() {
        return iAmDisconnected;
    }


    public boolean isPeerAlive(String ip) {
        //Checks if an ip address is incremented by more than half the amount of ips in the game.
        //In case the increment is more than the half, the node/peer will be labeled as disconnected.
        //TODO probably remove this method entirely
        return nodeInfoList.getStatusOfOneNode(ip);
    }

    public void reset(String ip) {
        //Sets the value of the Key ip to 0 as it resets its amount of fails.
        putIpInListIfNotAlready(ip);
        amountOfFailsPerIp.put(ip, 0);
    }

    public void iAmDisconnected() {
        //Can be called when the leader cant reach anyone but himself
        iAmDisconnected = true;

        notifyObserversIDied();
        //TODO remove println
        System.out.println("This machine can't reach anyone, so is disconnected");
    }

    private void notifyObserversIDied() {
        if (observers.size() > 0) {
            for (IConnectorObserver observer : observers) {
                if (observer instanceof IPlayerDisconnectedObserver) {
                    ((IPlayerDisconnectedObserver) observer).iAmDisconnected();
                }
            }
        }
    }

    private void notifyObserversPlayerDied(String ip) {
        if (observers.size() > 0) {
            for (IConnectorObserver observer : observers) {
                if (observer instanceof IPlayerDisconnectedObserver) {
                    ((IPlayerDisconnectedObserver) observer).playerIsDisconnected(ip);
                }
            }
        }
    }

    private void increment(String ip) {
        //Increments the value that is linked to the ip.
        putIpInListIfNotAlready(ip);
        amountOfFailsPerIp.put(ip, amountOfFailsPerIp.get(ip) + 1);
    }

    private void putIpInListIfNotAlready(String ip) {
        //Puts the ip in the hashmap if it is not yet in that list
        if (!amountOfFailsPerIp.containsKey(ip)) {
            add(ip);
        }
    }

    private void add(String ip) {
        //Adds the ip to the list
        amountOfFailsPerIp.put(ip, 0);
    }
}
