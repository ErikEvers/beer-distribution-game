package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.exceptions.NodeCantBeReachedException;
import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;
import org.han.ica.asd.c.socketrpc.SocketClient;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for making connections and sending objects using sockets.
 * This class is used by the 'FaultDetectorLeader', 'FaultDetectorPlayer' and the 'FaultResponder'.
 *
 * @author Oscar, Tarik
 * @see FaultDetectorLeader
 * @see FaultDetectorPlayer
 * @see FaultResponder
 */
public class FaultDetectionClient {
    private static final Logger LOGGER = Logger.getLogger(FaultDetectionClient.class.getName());
    private SocketClient socketClient;

    public FaultDetectionClient(){
        this.socketClient = new SocketClient();
    }

    /**
     * Tries to make a connection with the specified ipAddress.
     * It will try to send a 'PingMessage' to make sure it can reach the specified ipAddress.
     * If it can't reach an ipAddress it will throw an exception.
     * This method is used by the 'FaultDetectorLeader' and the 'FaultResponder'.
     *
     * @param ipAddress The ip address with which a connection will be made.
     * @throws NodeCantBeReachedException This exception is thrown when an ipAddress can't be reached.
     * @author Oscar, Tarik
     * @see FaultDetectorLeader
     * @see FaultResponder
     * @see PingMessage
     */
    public void makeConnection(String ipAddress) throws NodeCantBeReachedException {
        try {
            socketClient.makeConnection(ipAddress, new PingMessage());
        } catch (IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            throw new NodeCantBeReachedException(e);
        }

    }

    /**
     * Sends a 'FaultMessageResponse' to a specified ipAddress.
     * This method is used by the 'FaultResponder'. If an exception occurs when sending the message it will log the
     * message of the exception.
     *
     * @param faultMessageResponse The 'FaultMessageResponse' that is sent to the specified ipAdress.
     * @param ipToSendTo           The ip to send the 'FaultMessageResponse' to.
     * @author Oscar, Tarik
     * @see FaultResponder
     * @see FaultMessageResponse
     * @see NodeCantBeReachedException
     */
    public void sendFaultMessageResponse(FaultMessageResponse faultMessageResponse, String ipToSendTo) {
        try {
            sendObject(faultMessageResponse, ipToSendTo);
        } catch (NodeCantBeReachedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Sends a 'FaultMessage' to a specified ipAddress.
     * This method is used by the 'FaultDetectorLeader'. If an exception occurs when sending the message it will log the
     * message of the exception.
     *
     * @param faultMessage The 'FaultMessage' that is send to the specified ipAddress.
     * @param ipAddress    The ip to send the 'FaultMessage' to.
     * @author Oscar, Tarik
     * @see FaultMessage
     * @see NodeCantBeReachedException
     */
    public void sendFaultMessage(FaultMessage faultMessage, String ipAddress) {
        try {
            sendObject(faultMessage, ipAddress);
        } catch (NodeCantBeReachedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Sends a 'CanYouReachLeaderMessage' to a all Active ipAddresses.
     * This method is used by the 'FaultDetectorPlayer'. If an exception occurs when sending the message it will throw
     * the exception to the class that calls the method.
     *
     * @param canYouReachLeaderMessage The 'CanYouReachLeaderMessage' that is sent to the specified ipAddress.
     * @param ips                All ips to send the 'CanYouReachLeaderMessage' to.
     * @throws NodeCantBeReachedException The exception that is thrown if an exception occurred when sending the
     *                                    'CanYouReachLeaderMessage'
     * @author Tarik
     * @see CanYouReachLeaderMessage
     * @see FaultDetectorPlayer
     */
    public Map<String, Object> sendCanYouReachLeaderMessageToAll(String[] ips, CanYouReachLeaderMessage canYouReachLeaderMessage) {
        return socketClient.sendToAll(ips,canYouReachLeaderMessage);
    }

    /**
     * Sends the specified message to a specified ipAddress.
     * This method is used by the public methods in this class.
     * If it is unable to send the message it will throw a 'NodeCantBeReachedException'. And log this exception.
     *
     * @param object    The object to send to the specified ipAddress.
     * @param ipAddress The ip to send the object to.
     * @throws NodeCantBeReachedException The exception that is thrown when the specified object can't be sent to the
     *                                    specified ipAddress.
     * @author Oscar, Tarik
     * @see NodeCantBeReachedException
     */
    private void sendObject(Object object, String ipAddress) throws NodeCantBeReachedException {
        try {
            socketClient.sendObject(ipAddress,object);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
            throw new NodeCantBeReachedException(e);
        }
    }
}






