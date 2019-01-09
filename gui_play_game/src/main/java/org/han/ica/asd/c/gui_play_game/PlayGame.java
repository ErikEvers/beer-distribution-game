package org.han.ica.asd.c.gui_play_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.BeerGame;

public class PlayGame implements IGUIHandler {
    private BeerGame beerGame;

    @Override
    public void setData(Object[] data) {
        beerGame = (BeerGame) data[0];
    }

    @Override
    public void setupScreen() {
        PlayGameFactoryController controller = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/PlayGameFactory.fxml"));
        controller.setBeerGame(beerGame);
    }
}
