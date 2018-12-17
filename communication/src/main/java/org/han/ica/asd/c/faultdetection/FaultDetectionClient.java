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


public class FaultDetectionClient {
    private static final Logger logger = Logger.getLogger(FaultDetectionClient.class.getName());
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;

    public void makeConnection(String ipAddress) throws PeerCantBeReachedException {

        isConnected = false;

        while (!isConnected) {
            try (Socket socket = new Socket()) {

                socket.connect(new InetSocketAddress(ipAddress, 4445), 1000);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(new PingMessage());
                isConnected = true;
            } catch (SocketException | SocketTimeoutException se) {
                //logger.log(Level.INFO, se.getMessage(), se);
                throw new PeerCantBeReachedException(se);
            } catch (IOException e) {
                //logger.log(Level.INFO, e.getMessage(), e);
            }
        }
    }


    public void sendFaultMessageResponse(FaultMessageResponse faultMessageResponse, String ipToSendTo) {


        try {
            sendObject(faultMessageResponse, ipToSendTo);
        } catch (PeerCantBeReachedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void sendFaultMessage(FaultMessage faultMessage, String ipAddress) {
        try {
            sendObject(faultMessage, ipAddress);
        } catch (PeerCantBeReachedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void sendCanYouReachLeaderMessage(CanYouReachLeaderMessage canYouReachLeaderMessage, String ipAddress) throws PeerCantBeReachedException {
        sendObject(canYouReachLeaderMessage, ipAddress);
    }

    public void sendICanReachLeaderMessage(ICanReachLeaderMessage iCanReachLeaderMessage, String ipAddress) {
        try {
            sendObject(iCanReachLeaderMessage, ipAddress);
        } catch (PeerCantBeReachedException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }


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
                logger.log(Level.INFO, se.getMessage(), se);
                throw new PeerCantBeReachedException(se);
            }
        }

    }

}






