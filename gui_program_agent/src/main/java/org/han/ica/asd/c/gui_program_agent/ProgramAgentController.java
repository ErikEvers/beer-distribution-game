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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.han.ica.asd.c.businessrule.BusinessRuleHandler;
import org.han.ica.asd.c.businessrule.IBusinessRules;
import org.han.ica.asd.c.businessrule.parser.UserInputBusinessRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    TextArea businessRuleErrorTextArea;

    @FXML
    TextFlow businessRuleTexFlow;

    @FXML
    Button save;

    private IBusinessRules iBusinessRules;

    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

    private static final String AGENT_NAME_ERROR_HEADER = "Agent name Error";
    private static final String AGENT_NAME_ERROR_BODY = "You have to set a agent name.";

    private static final String BUSINESS_RULE_ERROR_HEADER = "Business rule Error";
    private static final String BUSINESS_RULE_ERROR_BODY = "There are no business rules given.\n Please enter them end try again.";

    private static final String BUSINESS_RULE_SUCCESS_HEADER = "Saved Successfully";
    private static final String BUSINESS_RULE_SUCCESS_BODY = "The business rules are saved successfully.";

    private static final String REGEX_SPLIT_ON_NEW_LINE = "\\r?\\n";

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
            stage.setScene(new Scene(root));
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
            String businessRulesUserInput = businessRuleInput.getText();

            if (agentName == null || agentName.trim().isEmpty()) {
                setProgramAgentPopup(AGENT_NAME_ERROR_HEADER, AGENT_NAME_ERROR_BODY, Color.RED);
            } else if (businessRulesUserInput == null || businessRulesUserInput.trim().isEmpty()) {
                setProgramAgentPopup(BUSINESS_RULE_ERROR_HEADER, BUSINESS_RULE_ERROR_BODY, Color.RED);
            } else {
                String[] lines = businessRulesUserInput.split(REGEX_SPLIT_ON_NEW_LINE);
                List<UserInputBusinessRule> businessRules = new ArrayList<>();
                for (int i = 0; i < lines.length; i++) {
                    businessRules.add(new UserInputBusinessRule(lines[i], i) {
                    });
                }
                    iBusinessRules.programAgent(agentName, businessRules);
                    setProgramAgentPopup(BUSINESS_RULE_SUCCESS_HEADER, BUSINESS_RULE_SUCCESS_BODY, Color.GREEN);

                    List<Integer> errorLines = e.getLinesWithError();
                    List<Text> textObjects = new ArrayList<>();
                    for (int i = 0; i < lines.length; i++) {
                        Text text = new Text(lines[i] + "\n");
                        if (errorLines.contains(i + 1)) {
                            text.setFill(Color.RED);
                        }
                        textObjects.add(text);
                    }
                    businessRuleErrorTextArea.clear();
                    businessRuleErrorTextArea.setText(e.toString());
                    businessRuleTexFlow.getChildren().clear();
                    businessRuleTexFlow.getChildren().addAll(textObjects);
                }
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
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        });
    }
}
