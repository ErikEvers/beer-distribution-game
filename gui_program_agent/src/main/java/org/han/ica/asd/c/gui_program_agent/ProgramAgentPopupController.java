package org.han.ica.asd.c.gui_program_agent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ProgramAgentPopupController implements ProgramAgentSharedController {

    @FXML
    AnchorPane mainContainer;

    @FXML
    Label headerLabel;

    @FXML
    Label bodyLabel;

    @FXML
    Button close;

    /***
     *  Function for initialising the current ProgramAgentInfo FXML. It also sets the actions of the button to close current window on click.
     */
    public void initialize() {
        mainContainer.getChildren().addAll();
        setCloseStageOnButton(close);
    }

    /***
     * Sets the color and the text of the header label.
     * @param text The text that the label should get.
     * @param headerColor The color of the label.
     */
    void setHeaderLabelText(String text, Color headerColor) {
        headerLabel.setText(text);
        headerLabel.setTextFill(headerColor);
    }

    /***
     *  Sets the text of the body label
     * @param text The text that the label should get.
     */
    void setBodyLabelText(String text) {
        bodyLabel.setText(text);
    }


}
