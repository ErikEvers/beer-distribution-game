package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.Global;
import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FaultDetectorPlayer extends TimerTask {
    @Inject private static Logger logger; //NOSONAR

    private long lastReceived;
    private boolean leaderIsPinging;
    private FaultDetectionClient faultDetectionClient;
    private FaultHandlerPlayer faultHandlerPlayer;
    private NodeInfoList nodeInfoList;
    private Timer timer;

    boolean getLeaderIsPinging() {
        return leaderIsPinging;
    }

    long getLastReceived() {
        return lastReceived;
    }

    FaultDetectorPlayer(NodeInfoList nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
        this.lastReceived = System.currentTimeMillis();

        faultHandlerPlayer = new FaultHandlerPlayer();
        faultDetectionClient = new FaultDetectionClient();

        leaderIsPinging = true;
    }

    public void start() {
        //running timer task as daemon thread
        timer = createTimer(true);
        timer.scheduleAtFixedRate(this, 0, Global.FaultDetectionInterval);
    }

    public Timer createTimer(Boolean isDeamon) {
        return new Timer(isDeamon);
    }

    public void run() {
        if (leaderIsPinging && leaderIsNotPinging()) {

            leaderIsPinging = false;

            faultHandlerPlayer.reset();

            askOtherPlayers();

            faultHandlerPlayer.whoIsDead();
        }
    }

    public boolean leaderIsNotPinging() {
        // timer check timestamp if client
        long current = System.currentTimeMillis();

        long timeDifference = current - lastReceived;

        long threeIntervals = Global.FaultDetectionInterval * 3;

        return timeDifference > threeIntervals;
    }

    private void askOtherPlayers() {

        List<String> ips = nodeInfoList.getActiveIpsWithoutLeader();
        faultHandlerPlayer.setAmountOfActiveIps(ips.size());

        Map<String, Object> response = faultDetectionClient.sendCanYouReachLeaderMessageToAll( ips.toArray(new String[0]), new CanYouReachLeaderMessage());
        int count = 0;
        for (Object responseMessage: response.values()) {
            count++;
            if (responseMessage instanceof Exception){
                faultHandlerPlayer.incrementAmountOfFailingIps();
            } else if(responseMessage instanceof CanYouReachLeaderMessageResponse) {
                CanYouReachLeaderMessageResponse canYouReachLeaderMessageResponse = (CanYouReachLeaderMessageResponse) responseMessage;
                if (canYouReachLeaderMessageResponse.getLeaderState()) {
                    faultHandlerPlayer.incrementAmountOfConnectionsWithLeader();
                }
            }
        }
    }

    public void pingMessageReceived(PingMessage pingMessage) {
        lastReceived = System.currentTimeMillis();
        leaderIsPinging = true;
    }

    public Object canYouReachLeaderMessageReceived(CanYouReachLeaderMessage canYouReachLeaderMessage) {
        boolean leaderIsAlive;

        if(leaderIsNotPinging()){
            leaderIsAlive = false;
        }else{
            try {
                faultDetectionClient.makeConnection("LEADERIP");
                leaderIsAlive = true;
            } catch (NodeCantBeReachedException e) {
                leaderIsAlive = false;
            }
        }
        return new CanYouReachLeaderMessageResponse(leaderIsAlive);
    }

    void setFaultDetectionClient(FaultDetectionClient faultDetectionClient) {
        this.faultDetectionClient = faultDetectionClient;
    }

    void setFaultHandlerPlayer(FaultHandlerPlayer faultHandlerPlayer) {
        this.faultHandlerPlayer = faultHandlerPlayer;
    }

    void setLastReceived(long lastReceived) {
        this.lastReceived = lastReceived;
    }

    void setLeaderIsPinging(boolean leaderIsPinging) {
        this.leaderIsPinging = leaderIsPinging;
    }
}
