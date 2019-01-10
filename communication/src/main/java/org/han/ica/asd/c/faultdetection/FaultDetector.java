package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;

import javax.inject.Inject;
import java.util.List;

public class FaultDetector {
    @Inject private FaultDetectionMessageReceiver faultDetectionMessageReceiver;
    @Inject private FaultDetectorPlayer faultDetectorPlayer;
    @Inject private FaultDetectorLeader faultDetectorLeader;
    @Inject private FaultResponder faultResponder;

    private List<IConnectorObserver> observers;

    public FaultDetector() {
        //For inject purposes
    }

    /**
     * This method starts the fault detection for the leader.
     * This should be started by the leader.
     *
     * @param nodeInfoList List with connected nodes to perform fault detection.
     * @author Tarik
     */
    public void startFaultDetectorLeader(NodeInfoList nodeInfoList) {
        faultDetectorLeader = makeFaultDetectorLeader(nodeInfoList, observers);
        faultDetectorLeader.start();
    }

    /**
     * This method starts the fault detection for the (non leader)player.
     * This should NOT be started by the leader.
     *
     * @param nodeInfoList List with connected nodes to perform fault detection.
     * @author Tarik
     */
    public void startFaultDetectorPlayer(NodeInfoList nodeInfoList) {
        faultResponder = makeFaultResponder();
        faultDetectorPlayer = makeFaultDetectorPlayer(nodeInfoList, observers);
        faultDetectorPlayer.start();
    }

    /**
     * Delegates the "FaultMessage' to the 'FaultResponder'.
     *
     * @param faultMessage The received message.
     * @param senderIp     The Ip of the sender.
     * @author Tarik
     * @see FaultDetectionMessageReceiver
     */
    public void faultMessageReceived(FaultMessage faultMessage, String senderIp) {
        if (faultResponder != null) {
            faultResponder.faultMessageReceived(faultMessage, senderIp);
        }
    }

    /**
     * Delegates the "FaultMessageResponse' to the 'FaultDetectorLeader'.
     *
     * @param faultMessageResponse The received message.
     * @author Tarik
     * @see FaultDetectionMessageReceiver
     */
    public void faultMessageResponseReceived(FaultMessageResponse faultMessageResponse) {
        if (faultDetectorLeader != null) {
            faultDetectorLeader.faultMessageResponseReceived(faultMessageResponse);
        }
    }

    /**
     * Delegates the "PingMessage' to the 'FaultDetectorPlayer'.
     *
     * @param pingMessage The received message.
     * @author Tarik
     * @see FaultDetectionMessageReceiver
     */
    public void pingMessageReceived(PingMessage pingMessage) {
        if (faultDetectorPlayer != null) {
            faultDetectorPlayer.pingMessageReceived(pingMessage);
        }
    }

    /**
     * Delegates the "CanYouReachLeaderMessage' to the 'FaultDetectorPlayer'.
     *
     * @param canYouReachLeaderMessage The received message.
     * @param senderIp                 The Ip of the sender.
     * @author Tarik
     * @see FaultDetectionMessageReceiver
     */
    public Object canYouReachLeaderMessageReceived(CanYouReachLeaderMessage canYouReachLeaderMessage, String senderIp) {
        if (faultDetectorPlayer != null) {
            return faultDetectorPlayer.canYouReachLeaderMessageReceived(canYouReachLeaderMessage, senderIp);
        }
        return null;
    }

    /**
     * Sets the 'NodeInfoList' and the list with observers in 'FaultDetectorLeader'.
     *
     * @param nodeInfoList List with connected nodes to perform fault detection.
     * @param observers    The list with the message observers.
     * @return 'FaultDetectorLeader' for testing purposes
     * @author Tarik
     */
    public FaultDetectorLeader makeFaultDetectorLeader(NodeInfoList nodeInfoList, List<IConnectorObserver> observers) {
        faultDetectorLeader.setObservers(observers);
        faultDetectorLeader.setNodeInfoList(nodeInfoList);
        return faultDetectorLeader;
    }

    /**
     * Sets the 'NodeInfoList' and the list with observers in 'FaultDetectorPlayer'.
     *
     * @param nodeInfoList List with connected nodes to perform fault detection.
     * @param observers    The list with the message observers.
     * @return 'FaultDetectorPlayer' for testing purposes
     * @author Tarik
     */
    public FaultDetectorPlayer makeFaultDetectorPlayer(NodeInfoList nodeInfoList, List<IConnectorObserver> observers) {
        faultDetectorPlayer.setObservers(observers);
        faultDetectorPlayer.setNodeInfoList(nodeInfoList);
        return faultDetectorPlayer;
    }

    /**
     * Sets the 'FaultResponder' for a second time when fault detector starts for testing purposes.
     * This was created before the GUICE implementation and could now be tested in a different ways.
     * It was initially made to create a new instance.
     *
     * @return 'FaultResponder' for testing purposes
     * @author Tarik
     */
    public FaultResponder makeFaultResponder() {
        return faultResponder;
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
     * Sets new faultDetectorLeader.
     *
     * @param faultDetectorLeader New value of faultDetectorLeader.
     */
    public void setFaultDetectorLeader(FaultDetectorLeader faultDetectorLeader) {
        this.faultDetectorLeader = faultDetectorLeader;
    }

    /**
     * Gets faultDetectorPlayer.
     *
     * @return Value of faultDetectorPlayer.
     */
    FaultDetectorPlayer getFaultDetectorPlayer() {
        return faultDetectorPlayer;
    }

    /**
     * Gets faultDetectorLeader.
     *
     * @return Value of faultDetectorLeader.
     */
    FaultDetectorLeader getFaultDetectorLeader() {
        return faultDetectorLeader;
    }

    /**
     * Gets faultResponder.
     *
     * @return Value of faultResponder.
     */
    FaultResponder getFaultResponder() {
        return faultResponder;
    }

    /**
     * Gets faultDetectionMessageReceiver.
     *
     * @return Value of faultDetectionMessageReceiver.
     */
    public FaultDetectionMessageReceiver getFaultDetectionMessageReceiver() {
        return faultDetectionMessageReceiver;
    }
}
