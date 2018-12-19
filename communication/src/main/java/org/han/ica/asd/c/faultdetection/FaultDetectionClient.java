package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.faultdetection.exceptions.PeerCantBeReachedException;
import org.han.ica.asd.c.faultdetection.messagetypes.CanYouReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.FaultMessageResponse;
import org.han.ica.asd.c.faultdetection.messagetypes.ICanReachLeaderMessage;
import org.han.ica.asd.c.faultdetection.messagetypes.PingMessage;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;

    /**
     * Tries to make a connection with the specified ipAddress.
     * It will try to send a 'PingMessage' to make sure it can reach the specified ipAddress.
     * If it can't reach an ipAddress it will throw an exception.
     * This method is used by the 'FaultDetectorLeader' and the 'FaultResponder'.
     *
     * @param ipAddress The ip address with which a connection will be made.
     * @throws PeerCantBeReachedException This exception is thrown when an ipAddress can't be reached.
     * @author Oscar, Tarik
     * @see FaultDetectorLeader
     * @see FaultResponder
     * @see PingMessage
     */
    public void makeConnection(String ipAddress) throws PeerCantBeReachedException {

        isConnected = false;

        while (!isConnected) {
            try (Socket socket = new Socket()) {

                socket.connect(new InetSocketAddress(ipAddress, 4445), 1000);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(new PingMessage());
                isConnected = true;
            } catch (SocketException | SocketTimeoutException se) {
                LOGGER.log(Level.INFO, se.getMessage(), se);
                throw new PeerCantBeReachedException(se);
            } catch (IOException e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
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
     * @see PeerCantBeReachedException
     */
    public void sendFaultMessageResponse(FaultMessageResponse faultMessageResponse, String ipToSendTo) {
        try {
            sendObject(faultMessageResponse, ipToSendTo);
        } catch (PeerCantBeReachedException e) {
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
     * @see PeerCantBeReachedException
     */
    public void sendFaultMessage(FaultMessage faultMessage, String ipAddress) {
        try {
            sendObject(faultMessage, ipAddress);
        } catch (PeerCantBeReachedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Sends a 'CanYouReachLeaderMessage' to a specified ipAddress.
     * This method is used by the 'FaultDetectorPlayer'. If an exception occurs when sending the message it will throw
     * the exception to the class that calls the method.
     *
     * @param canYouReachLeaderMessage The 'CanYouReachLeaderMessage' that is sent to the specified ipAddress.
     * @param ipAddress                The ip to send the 'CanYouReachLeaderMessage' to.
     * @throws PeerCantBeReachedException The exception that is thrown if an exception occurred when sending the
     *                                    'CanYouReachLeaderMessage'
     * @author Tarik
     * @see CanYouReachLeaderMessage
     * @see FaultDetectorPlayer
     */
    public void sendCanYouReachLeaderMessage(CanYouReachLeaderMessage canYouReachLeaderMessage, String ipAddress) throws PeerCantBeReachedException {
        sendObject(canYouReachLeaderMessage, ipAddress);
    }

    /**
     * Sends a 'iCanReachLeaderMessage' to a specified ipAddress.
     * This method is used by the 'FaultDetectorPlayer'
     * If an exception occurs when sending the 'iCanReachLeaderMessage' it will log the thrown exception.
     *
     * @param iCanReachLeaderMessage The 'iCanReachLeaderMessage' that is sent to a specified ipAddress.
     * @param ipAddress              The ip to send the 'iCanReadLeaderMessage' to.
     * @author Oscar, Tarik
     * @see ICanReachLeaderMessage
     * @see FaultDetectorPlayer
     */
    public void sendICanReachLeaderMessage(ICanReachLeaderMessage iCanReachLeaderMessage, String ipAddress) {
        try {
            sendObject(iCanReachLeaderMessage, ipAddress);
        } catch (PeerCantBeReachedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Sends the specified message to a specified ipAddress.
     * This method is used by the public methods in this class.
     * If it is unable to send the message it will throw a 'PeerCantBeReachedException'. And log this exception.
     *
     * @param object    The object to send to the specified ipAddress.
     * @param ipAddress The ip to send the object to.
     * @throws PeerCantBeReachedException The exception that is thrown when the specified object can't be sent to the
     *                                    specified ipAddress.
     * @author Oscar, Tarik
     * @see PeerCantBeReachedException
     */
    private void sendObject(Object object, String ipAddress) throws PeerCantBeReachedException {

        outputStream = null;
        isConnected = false;
        boolean isConnecting = true;

        while (!isConnected && isConnecting) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ipAddress, 4445), 2000);
                isConnected = true;

                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(object);

            } catch (IOException se) {
                LOGGER.log(Level.INFO, se.getMessage(), se);
                throw new PeerCantBeReachedException(se);
            }
        }
    }
}






