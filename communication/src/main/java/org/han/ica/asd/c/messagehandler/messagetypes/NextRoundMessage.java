package org.han.ica.asd.c.messagehandler.messagetypes;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.NEXT_ROUND_MESSAGE;

public class NextRoundMessage extends TransactionMessage {
    public NextRoundMessage() {
        super(NEXT_ROUND_MESSAGE);
    }
}
