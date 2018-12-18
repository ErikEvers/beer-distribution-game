package org.han.ica.asd.c.messagehandler.messagetypes;

import domainobjects.RoundModel;

import java.io.Serializable;

public class RoundModelMessage extends GameMessage implements Serializable{
    private RoundModel roundModel;
    private int commitStage;

    public RoundModelMessage(RoundModel roundModel, int commitStage) {
        super(2);
        this.roundModel = roundModel;
        this.commitStage = commitStage;
    }

    public RoundModel getRoundModel() {
        return roundModel;
    }

    public int getCommitStage() {
        return commitStage;
    }
}
