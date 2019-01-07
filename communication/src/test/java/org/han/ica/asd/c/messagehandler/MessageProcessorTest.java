package org.han.ica.asd.c.messagehandler;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.han.ica.asd.c.Connector;
import org.han.ica.asd.c.faultdetection.nodeinfolist.NodeInfoList;
import org.han.ica.asd.c.messagehandler.exceptions.LeaderNotPresentException;
import org.han.ica.asd.c.messagehandler.messagetypes.WhoIsTheLeaderMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MessageProcessorTest {

    @Mock
    private NodeInfoList nodeInfoList;

    private MessageProcessor messageProcessor;

    @Mock
    Connector connector;

    @BeforeEach
    void setup(){
        initMocks(this);
        messageProcessor = new MessageProcessor();

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

        assertTrue(result.getException() instanceof LeaderNotPresentException);

        LeaderNotPresentException leaderNotPresentException = (LeaderNotPresentException) result.getException();

        assertEquals( "I dont have a leader at this moment", leaderNotPresentException.getMessage());
    }

    @Test
    @DisplayName("Test if the correct nodeinfolist is retrieved")
    void TestIfTheCorrectNodeInfoListIsRetrieved(){
        messageProcessor.setConnector(connector);

        when(connector.getIps()).thenReturn(nodeInfoList);

        NodeInfoList result = messageProcessor.getNodeInfoListFromConnector();

        assertEquals(nodeInfoList, result);
    }
}
