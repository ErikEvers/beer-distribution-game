package org.han.ica.asd.c.messagehandler;


import org.han.ica.asd.c.Connector;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.messagehandler.exceptions.LeaderNotPresentException;
import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;

public class MessageProcessor {




    public MessageProcessor() {
    }


    public NodeInfoList getNodeInfoListFromConnector(){
        Connector connector = Connector.getInstance();
        return connector.getIps();
    }

    public WhoIsTheLeaderMessage whoIsTheLeaderMessageReceived(WhoIsTheLeaderMessage whoIsTheLeaderMessage) {
        NodeInfoList nodeInfoList = getNodeInfoListFromConnector();

        String leaderIp = nodeInfoList.getLeaderIp();
        if (leaderIp != null) {
            whoIsTheLeaderMessage.setResponse(leaderIp);
        }
        else{
            whoIsTheLeaderMessage.setResponse(new LeaderNotPresentException("I dont have a leader at this moment"));
        }
        return whoIsTheLeaderMessage;
    }


}