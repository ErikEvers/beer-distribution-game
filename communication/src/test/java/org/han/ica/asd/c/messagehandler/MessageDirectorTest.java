package org.han.ica.asd.c.messagehandler;

import org.han.ica.asd.c.MessageDirector;
import org.han.ica.asd.c.faultdetection.FaultDetectionMessageReceiver;
import org.han.ica.asd.c.messagehandler.messagetypes.GameMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.ResponseMessage;
import org.han.ica.asd.c.messagehandler.receiving.GameMessageReceiver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InvalidObjectException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class MessageDirectorTest {

    private MessageDirector messageDirector;

    @Mock
    private GameMessage gameMessage;

    @Mock
    private GameMessageReceiver gameMessageReceiver;

    @Mock
    private FaultDetectionMessageReceiver faultDetectionMessageReceiver;

    @BeforeEach
    void init() {
        initMocks(this);
        messageDirector = new MessageDirector();
        messageDirector.setGameMessageReceiver(gameMessageReceiver);
        messageDirector.setFaultDetectionMessageReceiver(faultDetectionMessageReceiver);
    }

    @Test
    void shouldReturnInvalidObjectMessage() {
        Object wrongObject = String.class;
        String expected = new InvalidObjectException("Invalid object").getMessage();

        ResponseMessage actual = (ResponseMessage) messageDirector.serverObjectReceived(wrongObject, "");

        assertEquals(actual.getException().getMessage(), expected);
    }

    @Test
    void shouldReturnSuccessfullObject() {
        ResponseMessage expected = new ResponseMessage(true);

        when(gameMessageReceiver.gameMessageReceived(gameMessage, "")).thenReturn(expected);

        ResponseMessage actual = (ResponseMessage) messageDirector.serverObjectReceived(gameMessage, "");

        assertEquals(actual.getIsSuccess(), expected.getIsSuccess());
    }
}
