package org.han.ica.asd.c.messagehandler;

import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class TransactionMessageTest {

    @Mock

    TurnModelMessage transactionMessage;

    public void init() {
        transactionMessage = new TurnModelMessage(any(roundData))
    }
}
