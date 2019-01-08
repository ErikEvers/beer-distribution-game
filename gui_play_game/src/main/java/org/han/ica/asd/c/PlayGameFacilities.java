package org.han.ica.asd.c;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;

public class PlayGameFacilities implements IGUIHandler {
    @Override
    public void setData(Object[] data) {

    }

    @Override
    public void setupScreen() {
        FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResources"), getClass().getResource("/fxml/PlayGameFacilities.fxml"));
    }
}
