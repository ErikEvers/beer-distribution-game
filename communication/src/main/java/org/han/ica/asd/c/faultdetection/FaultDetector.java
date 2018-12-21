package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.ICanReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;

import javax.inject.Inject;
import java.util.List;

public class FaultDetector {
    @Inject
    private FaultDetectionMessageReceiver faultDetectionMessageReceiver;

    @Inject
    private FaultDetectorPlayer faultDetectorPlayer;

    @Inject
    private FaultDetectorLeader faultDetectorLeader;

    @Inject
    private FaultResponder faultResponder;

    private List<IConnectorObserver> observers;


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

    public FaultDetector() {
        //Inject purposes
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

    public FaultDetectorLeader makeFaultDetectorLeader(NodeInfoList nodeInfoList, List<IConnectorObserver> observers) {
        faultDetectorLeader.setObservers(observers);
        faultDetectorLeader.setNodeInfoList(nodeInfoList);
        return faultDetectorLeader;
    }

    public FaultDetectorPlayer makeFaultDetectorPlayer(NodeInfoList nodeInfoList) {
        faultDetectorPlayer.setNodeInfoList(nodeInfoList);
        return faultDetectorPlayer;
    }

    public FaultResponder makeFaultResponder() {
        return faultResponder;
    }

    public void setObservers(List<IConnectorObserver> observers) {
        this.observers = observers;
    }
}
