package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.ICanReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;

import java.util.ArrayList;

public class FaultDetector {

    private FaultDetectionMessageReceiver faultDetectionMessageReceiver;
    private FaultDetectorPlayer faultDetectorPlayer;
    private FaultDetectorLeader faultDetectorLeader;
    private FaultResponder faultResponder;
    private ArrayList<IConnectorObserver> observers;


    FaultDetectorPlayer getFaultDetectorPlayer() {
        return faultDetectorPlayer;
    }

    FaultDetectorLeader getFaultDetectorLeader() {
        return faultDetectorLeader;
    }

    FaultResponder getFaultResponder() {
        return faultResponder;
    }

    public FaultDetectionMessageReceiver getFaultDetectionMessageReceiver() {
        return faultDetectionMessageReceiver;
    }

    public FaultDetector(ArrayList<IConnectorObserver> observers) {
        this.observers = observers;
        faultDetectionMessageReceiver = new FaultDetectionMessageReceiver(this);
    }

    public void setLeader(NodeInfoList nodeInfoList) {
        faultDetectorLeader = makeFaultDetectorLeader(nodeInfoList, observers);
        faultDetectorLeader.start();
    }

    public void setPlayer(NodeInfoList nodeInfoList) {
        faultResponder = makeFaultResponder();
        faultDetectorPlayer = makeFaultDetectorPlayer(nodeInfoList);
        faultDetectorPlayer.start();
    }

    public void faultMessageReceived(FaultMessage faultMessage, String senderIp) {
        if (faultResponder != null) {
            faultResponder.faultMessageReceived(faultMessage, senderIp);
        }
    }

    public void faultMessageResponseReceived(FaultMessageResponse faultMessageResponse) {
        if (faultDetectorLeader != null) {
            faultDetectorLeader.faultMessageResponseReceived(faultMessageResponse);
        }
    }

    public void pingMessageReceived(PingMessage pingMessage) {
        if (faultDetectorPlayer != null) {
            faultDetectorPlayer.pingMessageReceived(pingMessage);
        }
    }

    public void canYouReachLeaderMessageReceived(CanYouReachLeaderMessage canYouReachLeaderMessage, String senderIp) {
        if (faultDetectorPlayer != null) {
            faultDetectorPlayer.canYouReachLeaderMessageReceived(canYouReachLeaderMessage, senderIp);
        }
    }

    public void iCanReachLeaderMessageReceived(ICanReachLeaderMessage iCanReachLeaderMessage) {
        if (faultDetectorPlayer != null) {
            faultDetectorPlayer.iCanReachLeaderMessageReceived(iCanReachLeaderMessage);
        }
    }

    public FaultDetectorLeader makeFaultDetectorLeader(NodeInfoList nodeInfoList, ArrayList<IConnectorObserver> observers) {
        return new FaultDetectorLeader(nodeInfoList, observers);
    }

    public FaultDetectorPlayer makeFaultDetectorPlayer(NodeInfoList nodeInfoList) {
        return new FaultDetectorPlayer(nodeInfoList);
    }

    public FaultResponder makeFaultResponder() {
        return new FaultResponder();
    }
}
