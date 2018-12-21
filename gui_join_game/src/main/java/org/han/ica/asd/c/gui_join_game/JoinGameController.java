package org.han.ica.asd.c.gui_join_game;

import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class JoinGameController {
    private String localIp;

    @Inject
    @Named("ChooseFacility")
    private IGUIHandler chooseFacility;

    public void initialize(){
        try {
            setLocalIp(getLocalIp());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private String getLocalIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public void handleJoinGameButtonClick(){
        chooseFacility.setupScreen();
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }
}
