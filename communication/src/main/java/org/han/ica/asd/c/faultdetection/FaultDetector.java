package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultDetectionMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.interfaces.communication.IConnectorObserver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * This class is used initialize the FaultDetectors and delegates the received messaged to the correct class.
 *
 * @author Oscar, Tarik
 */
@Singleton
public class FaultDetector {

    private FaultDetectionMessageReceiver faultDetectionMessageReceiver;

    @Inject
    private FaultDetectorPlayer faultDetectorPlayer;
    @Inject
    private FaultDetectorLeader faultDetectorLeader;
    @Inject
    private FaultResponder faultResponder;

    private List<IConnectorObserver> observers;

    @Inject
    public FaultDetector(FaultDetectionMessageReceiver receiver) {
        //For inject purposes
        receiver.setFaultDetector(this);
        this.faultDetectionMessageReceiver = receiver;
    }

    /**
     * This method starts the fault detection for the leader.
     * If there already is an active faultdetector it stops it before starting a new one.
     * This should be started by the leader.
     *
     * @param nodeInfoList List with connected nodes to perform fault detection.
     * @author Tarik, Oscar
     */
    public void startFaultDetectorLeader(NodeInfoList nodeInfoList) {
        stopAllFaultDetectors();

        faultDetectorLeader = makeFaultDetectorLeader(nodeInfoList, observers);
        faultDetectorLeader.start();
    }

    /**
     * This method stops the fault detection for the for the leader.
     * This should be used when a player becomes a leader after leader migration.
     *
     * @author Tarik
     */
    public void stopFaultDetectorLeader() {
        faultDetectorLeader.stop();
    }

    /**
     * This method starts the fault detection for the (non leader)player.
     * If there already is an active faultdetector it stops it before starting a new one.
     * This should NOT be started by the leader.
     *
     * @param nodeInfoList List with connected nodes to perform fault detection.
     * @author Tarik, Oscar
     */
    public void startFaultDetectorPlayer(NodeInfoList nodeInfoList) {
        stopAllFaultDetectors();

        faultResponder = makeFaultResponder();
        faultDetectorPlayer = makeFaultDetectorPlayer(nodeInfoList, observers);
        faultDetectorPlayer.start();
        faultResponder.start();
    }

    /**
     * This methods stops all active faultdetectors.
     *
     * @authos Tarik
     */
    private void stopAllFaultDetectors(){
        if(faultDetectorLeader != null && faultDetectorLeader.isActive()){
            faultDetectorLeader.stop();
        }

        if(faultResponder != null && faultResponder.isActive()){
            faultResponder.stop();
        }
        if(faultDetectorPlayer != null && faultDetectorPlayer.isActive()){
            faultDetectorPlayer.stop();
        }
    }
    /**
     * This method stops the fault detection for the (non leader)player.
     * This should be used when a player becomes a leader after leader migration.
     *
     * @author Tarik
     */
    public void stopFaultDetectorPlayer() {
        faultDetectorPlayer.stop();
        faultResponder.stop();
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
        if (faultResponder.isActive()) {
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
        if (faultDetectorLeader.isActive()) {
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
        if (faultDetectorPlayer.isActive()) {
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
        if (faultDetectorPlayer.isActive()) {
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
     * @author Tarik, Oscar
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
     * @author Tarik, Oscar
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
     * @author Tarik, Oscar
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
     * Gets faultDetectionMessageReceiver.
     *
     * @return Value of faultDetectionMessageReceiver.
     */
    public FaultDetectionMessageReceiver getFaultDetectionMessageReceiver() {
        return faultDetectionMessageReceiver;
    }
}
