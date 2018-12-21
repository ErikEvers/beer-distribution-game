package org.han.ica.asd.c.gui_program_agent;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;

public class ProgramAgent implements IGUIHandler {
    public void setupScreen() {

        ResourceBundle resourceBundle = ResourceBundle.getBundle("languageResources");

        FXMLLoaderOnSteroids.getScreen(resourceBundle, getClass().getResource("/fxml/ProgramAgent.fxml"));
    }
}