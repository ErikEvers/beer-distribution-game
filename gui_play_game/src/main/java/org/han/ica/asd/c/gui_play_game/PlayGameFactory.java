package org.han.ica.asd.c.gui_play_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.model.domain_objects.Configuration;

import java.util.ResourceBundle;

public class PlayGameFactory implements IGUIHandler {
    private Configuration configuration;

    @Override
    public void setData(Object[] data) {
        configuration = (Configuration) data[0];
    }

    @Override
    public void setupScreen() {
        PlayGame controller = FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResources"), getClass().getResource("/fxml/PlayGameFactory.fxml"));
        controller.setConfiguration(configuration);
    }
}
