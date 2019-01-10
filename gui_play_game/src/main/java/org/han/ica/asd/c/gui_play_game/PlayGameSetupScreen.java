package org.han.ica.asd.c.gui_play_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ResourceBundle;

public class PlayGameSetupScreen implements IGUIHandler {
    BeerGame beerGame;

    @Inject @Named("PlayerComponent")
    IPlayerComponent playerComponent;

    @Override
    public void setData(Object[] data) {
        beerGame = (BeerGame) data[0];
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
            playGame.setBeerGame(beerGame);
        } else if (facilityNamePlayedByPlayer.equals(retailerName)) {
            playGame = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResources"), getClass().getResource("/fxml/PlayGameRetailer.fxml"));
            playGame.setBeerGame(beerGame);
        } else {
            playGame = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResources"), getClass().getResource("/fxml/PlayGameFacilities.fxml"));
            playGame.setBeerGame(beerGame);
            ((PlayGameFacilitiesController)playGame).setLblFacilitiesText(facilityNamePlayedByPlayer);
        }

        playGame.fillComboBox();
    }
}
