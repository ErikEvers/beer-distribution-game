package org.han.ica.asd.c.gui_program_agent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.han.ica.asd.c.businessrule.BusinessRuleHandler;
import org.han.ica.asd.c.businessrule.IBusinessRules;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProgramAgentController {

    @FXML
    AnchorPane mainContainer;

    @FXML
    Button moreInfo;

    @FXML
    TextField agentNameInput;

    @FXML
    TextArea businessRuleInput;

    @FXML
    Button save;

    private IBusinessRules iBusinessRules;

    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

    public ProgramAgentController() {
        iBusinessRules = new BusinessRuleHandler();
    }

    /***
     * Function for initialising the current ProgramAgent FXML. It also sets the actions of the button's
     */
    public void initialize() {
        mainContainer.getChildren().addAll();
        setMoreInfoButtonAction();
        setSaveButtonAction();
    }

    /***
     * For making a popup with a user defined message. Opens a new stage window.
     * @param headerText The text for the header of the popup
     * @param bodyText  The text for the body of the popup
     * @param headerColor   The color of the header text. To set for example red if its a error.
     */
    private void setProgramAgentPopup(String headerText, String bodyText, Color headerColor) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProgramAgentPopup.fxml"));
            root = loader.load();
            ProgramAgentPopupController programAgentPopupController = loader.getController();
            programAgentPopupController.setHeaderLabelText(headerText, headerColor);
            programAgentPopupController.setBodyLabelText(bodyText);
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 200));
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    /***
     * Set the button save to have the action that sets the popup.
     * And communicates with the IBusinessRules to program the agent.
     */
    private void setSaveButtonAction() {
        save.setOnAction(event -> {
            String agentName = agentNameInput.getText();
            String businessRules = businessRuleInput.getText();
            if (agentName == null || agentName.trim().isEmpty()) {
                setProgramAgentPopup("Agent name Error", "You have to set a agent name.", Color.RED);
            } else if (businessRules == null || businessRules.trim().isEmpty()) {
                setProgramAgentPopup("Business rule Error", "There are no business rules given.\n Please enter them end try again.", Color.RED);
            } else {
                iBusinessRules.programAgent(agentName, businessRules);
                setProgramAgentPopup("Saved Successfully", "The business rules are saved successfully.", Color.GREEN);
            }
        });
    }

    /***
     * Set the action for the MoreInfo Button.
     * It opens the new info stage.
     */
    private void setMoreInfoButtonAction() {
        moreInfo.setOnAction(event -> {
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml/ProgramAgentInfo.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 1000, 800));
                stage.show();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        });
    }
}
