package org.han.ica.asd.c.gui_program_agent;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;

public class ProgramAgentList implements IGUIHandler {

    @Override
    public void setData(Object[] data) {

        // implement interface

    }

    public void setupScreen() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languageResourcesGuiProgramAgent");

        FXMLLoaderOnSteroids.getScreen(resourceBundle, getClass().getResource("/fxml/ProgramAgentList.fxml"));
    }

    @Override
    public void updateScreen() {

    }
}
