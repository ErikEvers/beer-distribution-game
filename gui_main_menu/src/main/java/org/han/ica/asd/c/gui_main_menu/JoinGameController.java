package org.han.ica.asd.c.gui_main_menu;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class JoinGameController {
    private String localIp;

    public void initialize(){
        try {
            localIp = getLocalIp();
        } catch (UnknownHostException e) {
             
        }
    }

    private String getLocalIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
