package org.han.ica.asd.c.messagehandler.messagetypes;

import org.han.ica.asd.c.model.domain_objects.Round;

import java.io.Serializable;

public class RoundModelMessage extends GameMessage implements Serializable{
    private Round roundModel;
    private int commitStage;

    public RoundModelMessage(Round roundModel, int commitStage) {
        super(2);
        this.roundModel = roundModel;
        this.commitStage = commitStage;
    }

    public Round getRoundModel() {
        return roundModel;
    }

    public int getCommitStage() {
        return commitStage;
    }
}
