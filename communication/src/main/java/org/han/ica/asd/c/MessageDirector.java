package org.han.ica.asd.c;

import org.han.ica.asd.c.faultdetection.messagetypes.FaultDetectionMessage;
import org.han.ica.asd.c.faultdetection.FaultDetectionMessageReceiver;
import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.receiving.GameMessageReceiver;
import org.han.ica.asd.c.socketrpc.IServerObserver;

import java.io.InvalidObjectException;

public class MessageDirector implements IServerObserver {

    private GameMessageReceiver gameMessageReceiver;
    private FaultDetectionMessageReceiver faultDetectionMessageReceiver;


    public MessageDirector(GameMessageReceiver gameMessageReceiver, FaultDetectionMessageReceiver faultDetectionMessageReceiver) {//TODO Add FaultDetectionMessageReceiver as well
        this.gameMessageReceiver = gameMessageReceiver;
        this.faultDetectionMessageReceiver = faultDetectionMessageReceiver;
    }

    @Override
    public Object serverObjectReceived(Object receivedObject, String senderIp) {

        if (receivedObject instanceof GameMessage) {
            GameMessage gameMessage = (GameMessage) receivedObject;
            return gameMessageReceiver.gameMessageReceived(gameMessage);

        } else if (receivedObject instanceof FaultDetectionMessage) {
            FaultDetectionMessage faultDetectionMessage = (FaultDetectionMessage) receivedObject;
            faultDetectionMessageReceiver.receiveMessage(faultDetectionMessage, senderIp);

            //If any FaultDetectionMessages expect a response, return that response, don't return null
            //If any object is returned here, that object should be expected in the client that sent the original message
            return null;
        } else {
            return new ResponseMessage(false, new InvalidObjectException("Invalid object"));
        }
    }
}
