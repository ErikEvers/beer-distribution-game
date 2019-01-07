package org.han.ica.asd.c.gui_main_menu;


import org.han.ica.asd.c.fxml_helper.FXMLLoaderOnSteroids;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;


public class MainMenu implements IGUIHandler {

	public void setupScreen() {

		FXMLLoaderOnSteroids.getScreen(null, getClass().getResource("/fxml/MainMenu.fxml"));
	}

}
