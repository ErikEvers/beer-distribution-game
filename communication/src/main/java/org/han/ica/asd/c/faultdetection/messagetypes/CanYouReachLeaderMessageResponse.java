package org.han.ica.asd.c.faultdetection.messagetypes;

public class CanYouReachLeaderMessageResponse extends FaultDetectionMessage {
    public boolean getLeaderState() {
        return leaderState;
    }

    private boolean leaderState;

    public CanYouReachLeaderMessageResponse(boolean leaderState) {
        super(5);
        this.leaderState = leaderState;
    }

}