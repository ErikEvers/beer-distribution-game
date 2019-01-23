package org.han.ica.asd.c.gui_play_game;

import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

public class SelectAgent implements IGUIHandler {
    private int roundId;
    @Override
    public void setData(Object[] data) {
        roundId = (int) data[0];
    }

    @Override
    public void setupScreen() {
        SelectAgentController controller = FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/SelectAgent.fxml"));
        controller.setRoundId(roundId);
    }

    @Override
    public void updateScreen() {

    }
}