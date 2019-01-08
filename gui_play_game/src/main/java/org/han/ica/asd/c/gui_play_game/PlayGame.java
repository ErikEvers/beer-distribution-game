package org.han.ica.asd.c.gui_play_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Configuration;

public class PlayGame implements IGUIHandler {
    Configuration configuration;

    @Override
    public void setData(Object[] data) {
        configuration = (Configuration) data[0];
    }

    @Override
    public void setupScreen() {
        PlayGameFactoryController controller = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/PlayGameFactory.fxml"));
        controller.setConfiguration(configuration);
    }
}
