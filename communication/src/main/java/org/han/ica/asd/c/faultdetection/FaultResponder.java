package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;

import javax.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to respond to 'FaultMessages'. It checks whether it can connect with te failing ip in the message
 * and sends a boolean as response
 *
 * @author Tarik, Oscar
 */
public class FaultResponder {
    @Inject
    private static Logger logger;
    @Inject
    private FaultDetectionClient faultDetectionClient;
    private boolean active;

    public FaultResponder() {
        //For inject purposes
    }

    /**
     * This message creates the thread and starts it after the daemon is set to true
     * so it will close properly.
     *
     * @param faultMessage The message received from the leader.
     * @param senderIp     The ip of the sender.
     * @author Tarik
     */
    public void faultMessageReceived(FaultMessage faultMessage, String senderIp) {
        Thread t = sendResponseInAThread(faultMessage, senderIp);
        t.setDaemon(true);
        t.start();
    }

    /**
     * This method creates the thread with a call to send a response message.
     *
     * @param faultMessage This message contains the ip the leader couldn't reach.
     * @param senderIp     The ip of the sender.
     * @return the created thread
     * @author Oscar
     */
    public Thread sendResponseInAThread(FaultMessage faultMessage, String senderIp) {
        return new Thread(() -> sendResponse(faultMessage, senderIp));
    }

    /**
     * This method checks whether it can make a socket connection with the failing ip. And sends a response with a boolean.
     *
     * @param faultMessage The message contents needs to be used before sending a response.
     * @param senderIp     Is used to send the response to the correct Address.
     * @author Tarik
     */
    public void sendResponse(FaultMessage faultMessage, String senderIp) {
        String failingIp = faultMessage.getIp();
        try {
            faultDetectionClient.makeConnection(failingIp);
            faultDetectionClient.sendFaultMessageResponse(new FaultMessageResponse(true, failingIp), senderIp);

        } catch (NodeCantBeReachedException e) {
            logger.log(Level.INFO, e.getMessage(), e);
            faultDetectionClient.sendFaultMessageResponse(new FaultMessageResponse(false, failingIp), senderIp);
        }
    }

    /**
     * Sets the faultDetectionClient for test purposes.
     *
     * @param faultDetectionClient mock/spy
     */
    void setFaultDetectionClient(FaultDetectionClient faultDetectionClient) {
        this.faultDetectionClient = faultDetectionClient;
    }

    /**
     * Sets the 'FaultResponder' to active.
     *
     * @author Tarik
     */
    public void start() {
        this.active = true;
    }

    /**
     * Sets the 'FaultResponder' to inactive.
     *
     * @author Tarik
     */
    public void stop() {
        this.active = false;
    }

    /**
     * Returns the 'FaultDetectorLeader' state.
     *
     * @return isActive
     */
    public boolean isActive() {
        return active;
    }
}
