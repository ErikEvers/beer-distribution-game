package org.han.ica.asd.c.gui_join_game;

import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JoinGameController {
    private String localIp;
    private static final Logger LOGGER = Logger.getLogger(org.han.ica.asd.c.gui_join_game.JoinGameController.class.getName());

    @Inject
    @Named("ChooseFacility")
    private IGUIHandler chooseFacility;

    @Inject
    @Named("MainMenu")
    private IGUIHandler mainMenu;

    public void initialize(){
        try {
            setLocalIp(getLocalIp());
        } catch (UnknownHostException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
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

    public void handleBackToMenuButtonClick() {
        mainMenu.setupScreen();
    }
}
