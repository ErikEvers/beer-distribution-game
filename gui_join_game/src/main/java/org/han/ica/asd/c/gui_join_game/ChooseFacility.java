package org.han.ica.asd.c.gui_join_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;

public class ChooseFacility implements IGUIHandler {
    @Override
    public void setData(Object[] data) {
// Methods from super
    }

    @Override
    public void setupScreen() {
        FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResources"), getClass().getResource("/fxml/ChooseFacility.fxml"));
    }

    @Override
    public void updateScreen() {

    }
}
