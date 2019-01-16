package org.han.ica.asd.c.gui_play_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

public class SelectAgent implements IGUIHandler {

    @Override
    public void setData(Object[] data) {
    }

    @Override
    public void setupScreen() {
        SelectAgentController controller = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/SelectAgent.fxml"));
    }
}