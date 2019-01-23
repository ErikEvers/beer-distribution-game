package org.han.ica.asd.c.messagehandler;


import org.han.ica.asd.c.Connector;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.exceptions.communication.LeaderNotPresentException;
import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;

import javax.inject.Inject;

/**
 * Most messages are redirected to be processed by other components, but messages that can be handled by this component will be processed here.
 */

public class MessageProcessor {

    static Connector connector;

    /**
     * Gets the current 'NodeInfoList' from the connector instance.
     *
     * @author Oscar
     * @return The current 'NodeInfoList'.
     * @see Connector
     * @see NodeInfoList
     */
    public NodeInfoList getNodeInfoListFromConnector() {
        return connector.getIps();
    }

    /**
     * Check if it has a leader momentarily. If it does it sets the response value in the 'WhoIsTheLeaderMessage'.
     * If it doesnt it sets the Exception value in the GameMessage.
     *
     * @author Oscar
     * @param whoIsTheLeaderMessage The 'WhoIsTheLeaderMessage' that was received, and needs to be processed.
     * @return The processed 'WhoIsTheLeaderMessage' with either the Response or the Exception value set.
     */
    public WhoIsTheLeaderMessage whoIsTheLeaderMessageReceived(WhoIsTheLeaderMessage whoIsTheLeaderMessage) {
        NodeInfoList nodeInfoList = getNodeInfoListFromConnector();

        String leaderIp = nodeInfoList.getLeaderIp();
        if (leaderIp != null) {
            whoIsTheLeaderMessage.setResponse(leaderIp);
        } else {
            whoIsTheLeaderMessage.setException(new LeaderNotPresentException("I dont have a leader at this moment"));
        }
        return whoIsTheLeaderMessage;
    }

    /**
     * Sets new connector.
     *
     * @param connector New value of connector.
     */

    public static void setConnector(Connector connector) {
        MessageProcessor.connector = connector;
    }
}