package org.han.ica.asd.c.messagehandler;


import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.messagehandler.exceptions.LeaderNotPresentException;
import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageProcessorTest {

    private NodeInfoList nodeInfoList = mock(NodeInfoList.class);


    @BeforeEach
    void setup(){

    }

    @Test
    @DisplayName("Test response is set to leaderIp when leaderIp is not null")
    void whoIsTheLeaderMessageReceivedTestResponseHappyFlow(){

        MessageProcessor messageProcessorMock = new MessageProcessor(){
            @Override
            public NodeInfoList getNodeInfoListFromConnector() {
                return nodeInfoList;
            }
        };

        String testIp = "TestIp";

        when(nodeInfoList.getLeaderIp()).thenReturn(testIp);

        WhoIsTheLeaderMessage whoIsTheLeaderMessage = new WhoIsTheLeaderMessage();

        WhoIsTheLeaderMessage result = messageProcessorMock.whoIsTheLeaderMessageReceived(whoIsTheLeaderMessage);

        assertEquals(testIp, result.getResponse());
    }

    @Test
    @DisplayName("Test if the reponse is set to the exception when leaderIp is null")
    void whoIsTheLeaderMessageReceiveedTestResponseUnHappyFlow(){
        MessageProcessor messageProcessorMock = new MessageProcessor(){
            @Override
            public NodeInfoList getNodeInfoListFromConnector() {
                return nodeInfoList;
            }
        };

        when(nodeInfoList.getLeaderIp()).thenReturn(null);

        WhoIsTheLeaderMessage whoIsTheLeaderMessage = new WhoIsTheLeaderMessage();
        WhoIsTheLeaderMessage result = messageProcessorMock.whoIsTheLeaderMessageReceived(whoIsTheLeaderMessage);

        assertTrue(result.getResponse() instanceof LeaderNotPresentException);

        LeaderNotPresentException leaderNotPresentException = (LeaderNotPresentException) result.getResponse();

        assertEquals( "I dont have a leader at this moment", leaderNotPresentException.getMessage());

    }
}
