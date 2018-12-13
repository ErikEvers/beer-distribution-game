package org.han.ica.asd.c.gameleader.componentInterfaces;

public abstract class IParticipant {
    int facilityId;

    public IParticipant(int facilityId){
        this.facilityId = facilityId;
    }

    public void doOrder() {

    }
}
