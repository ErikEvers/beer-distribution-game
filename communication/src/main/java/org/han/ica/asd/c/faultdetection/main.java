package org.han.ica.asd.c.faultdetection;

import org.han.ica.asd.c.Connector;

public class main {

    private static Connector connector;

        public static void main(String[] args) {
            connector = Connector.getInstance();
            //aanmeldenSpel();
            startenSpel();
            while(true){}
        }


    public static void aanmeldenSpel() {
        connector.setJoiner();
    }

    public static void startenSpel() {
        connector.addToNodeInfoList("169.254.156.128");
        connector.addToNodeInfoList("169.254.153.134");
        connector.addToNodeInfoList("192.168.137.91");
        connector.addToNodeInfoList("192.168.137.91");

        connector.setLeader();
    }

}
