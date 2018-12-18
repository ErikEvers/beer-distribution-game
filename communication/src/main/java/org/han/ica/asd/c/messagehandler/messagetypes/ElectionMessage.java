package org.han.ica.asd.c.messagehandler.messagetypes;

import domainobjects.Election;

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
