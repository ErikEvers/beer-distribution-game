package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.interface_models.Election;

public class ElectionMessage extends GameMessage {
    private Election election;

    public ElectionMessage(Election election) {
        super(3);
        this.election = election;
    }

    public Election getElection() {
        return election;
    }
}
