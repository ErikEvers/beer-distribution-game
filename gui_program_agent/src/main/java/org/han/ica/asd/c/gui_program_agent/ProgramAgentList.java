package org.han.ica.asd.c.gui_program_agent;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;

public class ProgramAgentList implements IGUIHandler {

    @Override
    public void setData(Object[] data) {

    }

    public void setupScreen() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languageResources");

        FXMLLoaderOnSteroids.getScreen(resourceBundle, getClass().getResource("/fxml/ProgramAgentList.fxml"));
    }
}
