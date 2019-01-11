package org.han.ica.asd.c.faultdetection.messagetypes;

import org.han.ica.asd.c.faultdetection.FaultDetectionClient;
import org.han.ica.asd.c.faultdetection.FaultDetectionMessageReceiver;

/**
 * This message is send by the leader to all the nodes it can reach.
 * This message is used to send the ip address of a specific node that the leader was unable to reach.<br>
 * The leader sends this message to check whether the node has completely failed or whether only the leader can
 * not reach the specific node.<br>
 * The leader sends this message to all the nodes he can reach, basically asking if the nodes he can reach are able to
 * reach the specific node the leader was unable to reach.
 *
 * @author Tarik
 * @see FaultDetectionClient
 * @see FaultDetectionMessage
 * @see FaultDetectionMessageReceiver
 */

public class FaultMessage extends FaultDetectionMessage {
    private String ip;

    public FaultMessage(String ip) {
        super(1);
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

}