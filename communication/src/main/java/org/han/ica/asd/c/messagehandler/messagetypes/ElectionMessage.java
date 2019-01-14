package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.interface_models.ElectionModel;

import static org.han.ica.asd.c.messagehandler.messagetypes.MessageIds.ELECTION_MESSAGE;

public class ElectionMessage extends GameMessage {
    private ElectionModel election;

    public ElectionMessage(ElectionModel election) {
        super(ELECTION_MESSAGE);
        this.election = election;
    }

    public ElectionModel getElection() {
        return election;
    }
}
