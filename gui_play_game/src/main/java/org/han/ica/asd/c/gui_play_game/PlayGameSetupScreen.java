package org.han.ica.asd.c.gui_play_game;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.Player;

import java.util.ResourceBundle;

public class PlayGameSetupScreen implements IGUIHandler {

    @Inject @Named("PlayerComponent")
    IPlayerComponent playerComponent;

    @Override
    public void setData(Object[] data) {

    }

    @Override
    public void setupScreen() {
        final String factoryName = "Factory";
        final String retailerName = "Retailer";
        Player player = playerComponent.getPlayer();

        PlayGame playGame;

        String facilityNamePlayedByPlayer = player.getFacility().getFacilityType().getFacilityName();
        if (facilityNamePlayedByPlayer.equals(factoryName)) {
            playGame = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResources"), getClass().getResource("/fxml/PlayGameFactory.fxml"));
        } else if (facilityNamePlayedByPlayer.equals(retailerName)) {
            playGame = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResources"), getClass().getResource("/fxml/PlayGameRetailer.fxml"));
        } else {
            playGame = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResources"), getClass().getResource("/fxml/PlayGameFacilities.fxml"));
            ((PlayGameFacilitiesController)playGame).setLblFacilitiesText(facilityNamePlayedByPlayer);
        }

        playerComponent.setUi(playGame);
    }
}