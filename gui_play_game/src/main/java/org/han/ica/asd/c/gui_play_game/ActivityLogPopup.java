package org.han.ica.asd.c.gui_play_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.BeerGame;

public class ActivityLogPopup implements IGUIHandler {
    BeerGame beerGame;
    int facilityId;
    ActivityLogPopupController activityLogPopupController;
    @Override
    public void setData(Object[] data) {
        beerGame = (BeerGame) data[0];
        facilityId = (int) data[1];
    }

    @Override
    public void setupScreen() {
        activityLogPopupController = FXMLLoaderOnSteroids.getPopupScreen(null, getClass().getResource("/fxml/ActivityLogPopup.fxml"),null);
        activityLogPopupController.setLogContent(beerGame,facilityId);
    }

    @Override
    public void updateScreen() {
        activityLogPopupController.setLogContent(beerGame,facilityId);
    }
}
