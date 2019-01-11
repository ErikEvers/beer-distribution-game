package org.han.ica.asd.c;

import org.han.ica.asd.c.faultdetection.messagetypes.FaultDetectionMessage;
import org.han.ica.asd.c.faultdetection.FaultDetectionMessageReceiver;
import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.receiving.GameMessageReceiver;
import org.han.ica.asd.c.socketrpc.IServerObserver;

import javax.inject.Inject;
import java.io.InvalidObjectException;

public class MessageDirector implements IServerObserver {

    @Inject
    private GameMessageReceiver gameMessageReceiver;

    @Inject
    private FaultDetectionMessageReceiver faultDetectionMessageReceiver;

    public MessageDirector() {
        //for inject purposes
    }

    /**
     * This method receives an object from a socket connection and delegates the object to GameMessageReceiver.
     * If any FaultDetectionMessages expect a response, return that response, don't return null.
     * If any object is returned here, that object should be expected in the client that sent the original message.
     * @param receivedObject
     * @return Object
     */
    @Override
    public Object serverObjectReceived(Object receivedObject, String senderIp) {

        if (receivedObject instanceof GameMessage) {
            GameMessage gameMessage = (GameMessage) receivedObject;
            return gameMessageReceiver.gameMessageReceived(gameMessage, senderIp);

        } else if (receivedObject instanceof FaultDetectionMessage) {
            FaultDetectionMessage faultDetectionMessage = (FaultDetectionMessage) receivedObject;
            return faultDetectionMessageReceiver.receiveMessage(faultDetectionMessage, senderIp);
        } else {
            return new ResponseMessage(false, new InvalidObjectException("Invalid object"));
        }
    }

    @Override
    public void setGameMessageReceiver(GameMessageReceiver gameMessageReceiver) {
        this.gameMessageReceiver = gameMessageReceiver;
    }

    public void setFaultDetectionMessageReceiver(FaultDetectionMessageReceiver faultDetectionMessageReceiver) {
        this.faultDetectionMessageReceiver = faultDetectionMessageReceiver;
    }
}
