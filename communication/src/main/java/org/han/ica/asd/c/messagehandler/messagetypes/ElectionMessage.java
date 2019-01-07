package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.interface_models.ElectionModel;

public class ElectionMessage extends GameMessage {
    private ElectionModel election;
    private static final int ELECTION_MESSAGE = 3;

    public ElectionMessage(ElectionModel election) {
        super(ELECTION_MESSAGE);
        this.election = election;
    }

    public ElectionModel getElection() {
        return election;
    }
}
