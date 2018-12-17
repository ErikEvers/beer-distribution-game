package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.exceptions.PeerCantBeReachedException;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.Global;
import org.han.ica.asd.c.observers.IConnectorObserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class FaultDetectorLeader extends TimerTask {
    //TODO make the nodeinfolist the same over every class
    private NodeInfoList nodeInfoList;
    private FaultDetectionClient faultDetectionClient;
    private FaultHandlerLeader faultHandlerLeader;
    private List<String> ips;
    private FailLog failLog;
    private Timer timer;

    FaultDetectorLeader(NodeInfoList nodeInfoList, ArrayList<IConnectorObserver> observers) {
        this.nodeInfoList = nodeInfoList;
        faultHandlerLeader = new FaultHandlerLeader(nodeInfoList, observers);
        faultDetectionClient = new FaultDetectionClient();
        failLog = new FailLog(nodeInfoList);
    }

    @Override
    public void run() {
        //TODO remove the printlns.
        //Tries to make the connection once every set interval.
        ips = nodeInfoList.getActiveIps();
        for (String ip : ips) {
            System.out.println("Sending Ping to : " + ip + " : " + new Date());
            makeConnection(ip);
            System.out.println("Ping Sent:" + new Date());
        }

        //Checks if node wasn't reached 3 times, it then sends a faultMessage to all peers that can be reached.
        sendFaultMessagesToActivePlayers(ips);

        checkIfThisMachineIsDisconnected();
    }

    public void start() {
        //running timer task as daemon thread
        timer = createTimer(true);
        timer.scheduleAtFixedRate(this, 0, Global.FaultDetectionInterval);
    }

    public Timer createTimer(Boolean isDeamon) {
        return new Timer(isDeamon);
    }

    public void sendFaultMessagesToActivePlayers(List<String> ips) {
        for (String ip : ips) {
            if (failLog.checkIfIpIsFailed(ip)) {
                sendFaultMessage(ip);
            }
        }
    }

    public void checkIfThisMachineIsDisconnected() {
        if (failLog.getSuccesSize() <= 1) {
            //If the Leader can only reach himself and noone else, he is probably disconnected so no longer leader.
            faultHandlerLeader.iAmDisconnected();
            timer.cancel();
            timer.purge();
        }
    }

    public void faultMessageResponseReceived(FaultMessageResponse faultMessageResponse) {
        Boolean isAlive = faultMessageResponse.getAlive();
        String ip = faultMessageResponse.getIpOfSubject();

        if (!isAlive) {
            faultHandlerLeader.incrementFailure(ip);
        }
    }

    private void makeConnection(String ip) {
        //Makes a socket connection for the given Ip.
        //If the connection doesnt throw an exception, the amount of fails is reset on the failLog.
        //It then resets the amount of unsuccesfull connections per peer on the faultHandlerLeader.
        //It then updates the nodeinfoList and sets the value of isConnected to true on the specific node.
        try {
            faultDetectionClient.makeConnection(ip);
            failLog.reset(ip);
            faultHandlerLeader.reset(ip);
            nodeInfoList.updateIsConnected(ip, true);
        } catch (PeerCantBeReachedException e) {
            if (!failLog.checkIfIpIsFailed(ip)) {
                failLog.increment(ip);
            }
        }
    }

    private void sendFaultMessage(String failingIp) {
        //Checks for every ip if it is alive(without failures) it then sends a faultmessage to that ip.
        for (String ip : ips) {
            if (failLog.isAlive(ip)) {
                faultDetectionClient.sendFaultMessage(new FaultMessage(failingIp), ip);
            }
        }
    }

    void setFaultDetectionClient(FaultDetectionClient faultDetectionClient) {
        this.faultDetectionClient = faultDetectionClient;
    }

    void setFaultHandlerLeader(FaultHandlerLeader faultHandlerLeader) {
        this.faultHandlerLeader = faultHandlerLeader;
    }

    void setFailLog(FailLog failLog) {
        this.failLog = failLog;
    }

    void setIps(List<String> ips) {
        this.ips = ips;
    }

    void setTimer(Timer t) {
        this.timer = t;
    }
}
