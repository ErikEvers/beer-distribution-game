package org.han.ica.asd.c.gui_program_agent;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;

public class ProgramAgent implements IGUIHandler {

    private String agentName;

    public void setData(Object[] data) {
        this.agentName = (String)data[0];

    }

    public void setupScreen() {

        ResourceBundle resourceBundle = ResourceBundle.getBundle("languageResourcesGuiProgramAgent");

        ProgramAgentController controller = FXMLLoaderOnSteroids.getScreen(resourceBundle, getClass().getResource("/fxml/ProgramAgent.fxml"));

        controller.setAgentName(agentName);
    }
}