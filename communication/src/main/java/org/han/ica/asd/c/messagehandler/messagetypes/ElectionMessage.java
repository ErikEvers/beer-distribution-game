package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.interface_models.ElectionModel;

public class ElectionMessage extends GameMessage {
    private ElectionModel election;

    public ElectionMessage(ElectionModel election) {
        super(3);
        this.election = election;
    }

    public ElectionModel getElection() {
        return election;
    }
}
