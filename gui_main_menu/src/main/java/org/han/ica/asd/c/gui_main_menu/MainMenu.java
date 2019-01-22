package org.han.ica.asd.c.gui_main_menu;


import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;

import java.util.ResourceBundle;


public class MainMenu implements IGUIHandler {

	@Override
	public void setData(Object[] data) {
		// implement interface
	}


	public void setupScreen() {
		FXMLLoaderOnSteroids.getScreen(ResourceBundle.getBundle("languageResourcesGuiMainMenu"), getClass().getResource("/fxml/MainMenu.fxml"));
	}

	@Override
	public void updateScreen() {

	}

}
