package org.han.ica.asd.c.faultdetection.messagetypes;

public class FaultMessage extends FaultDetectionMessage {

    private String ip;
    public FaultMessage(String ip){
        super(1);
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

}