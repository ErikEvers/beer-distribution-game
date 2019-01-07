package org.han.ica.asd.c.messagehandler;

import org.han.ica.asd.c.messagehandler.messagetypes.RoundModelMessage;
import org.han.ica.asd.c.messagehandler.messagetypes.TurnModelMessage;
import org.han.ica.asd.c.model.domain_objects.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class TransactionMessageTest {

    @Mock
    private Round round;

    private RoundModelMessage transactionMessage;

    private final int COMMIT_PHASE = 1;
    private final int STAGE_PHASE = 0;
    private final int ROLLBACK_PHASE = -1;

    @BeforeEach
    public void init() {
        transactionMessage = new RoundModelMessage(round);
    }

    @Test
    public void testPhaseToCommit() {
        transactionMessage.setPhaseToCommit();
        assertEquals(transactionMessage.getPhase(), COMMIT_PHASE);
    }

    @Test
    public void testPhaseToStage() {
        transactionMessage.setPhaseToStage();
        assertEquals(transactionMessage.getPhase(), STAGE_PHASE);
    }

    @Test
    public void testPhaseToRollback() {
        transactionMessage.setPhaseToRollback();
        assertEquals(transactionMessage.getPhase(), ROLLBACK_PHASE);
    }
}
