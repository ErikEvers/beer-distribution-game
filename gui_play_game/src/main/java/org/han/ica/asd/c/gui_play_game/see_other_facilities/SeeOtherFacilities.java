package org.han.ica.asd.c.gui_play_game.see_other_facilities;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;
import java.util.ArrayList;

public class SeeOtherFacilities implements IGUIHandler {
    ArrayList<Double> coordinates;
    SeeOtherFacilitiesController seeOtherFacilitiesController;
    @Override
    public void setData(Object[] data) {
        coordinates = (ArrayList<Double>) data[0];
    }

    @Override
    public void setupScreen() {
       seeOtherFacilitiesController = FXMLLoaderOnSteroids.getPopupScreen(ResourceBundle.getBundle("languageResources"), getClass().getResource("/fxml/SeeOtherFacilities.fxml"),coordinates);
    }

    public void updateScreen(){
        seeOtherFacilitiesController.updateTreeBuilder();
    }
}
