package org.han.ica.asd.c.gui_program_agent;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRules;
import org.han.ica.asd.c.model.interface_models.UserInputBusinessRule;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProgramAgentController {
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

    @Inject
    private IBusinessRules iBusinessRules;

    @Inject
    @Named("ProgramAgentList")
    IGUIHandler programAgentList;

    @Inject
    @Named("BusinessruleStore")
    IBusinessRuleStore iBusinessRuleStore;

    private ObservableList<String> items;

    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

    private ResourceBundle resourceBundle;

    public void setAgentName(String name) {
        if(name != null) {
            save.setDisable("Default".equals(name));

            agentNameInput.setText(name);
            List<String> rules  = iBusinessRuleStore.readInputBusinessRules(name);
            for (String rule: rules) {
                businessRuleInput.appendText(rule + "\n");
            }
            agentNameInput.setDisable(true);
        }
    }

    /***
     * Function for initialising the current ProgramAgent FXML. It also sets the actions of the button's
     */
    public void initialize() {
        resourceBundle = ResourceBundle.getBundle("languageResourcesGuiProgramAgent");
    }

    /***
     * For making a popup with a user defined message. Opens a new stage window.
     *
     * @param headerText The text for the header of the popup
     * @param bodyText  The text for the body of the popup
     * @param headerColor   The color of the header text. To set for example red if its a error.
     */
    private void setProgramAgentPopup(String headerText, String bodyText, Color headerColor) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProgramAgentPopup.fxml"), resourceBundle);
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
    @FXML
    private void saveButtonAction() {
        clearOldScreenValues();
        String agentName = agentNameInput.getText();

        String businessRulesUserInput = businessRuleInput.getText();
        if (checkIfStringEmpty(agentName)) {
            setProgramAgentPopup(resourceBundle.getString("agent_name_error_header"), resourceBundle.getString("agent_name_error_body"), Color.RED);
        } else if (checkIfStringEmpty(businessRulesUserInput)) {
            setProgramAgentPopup(resourceBundle.getString("business_rule_error_header"), resourceBundle.getString("business_rule_error_body"), Color.RED);
        } else if (!isUniqueAgentName(agentName)) {
            setProgramAgentPopup(resourceBundle.getString("unique_agent_name_error_header"), resourceBundle.getString("unique_agent_name_error_body"), Color.RED);
        } else {
            List<UserInputBusinessRule> result = iBusinessRules.programAgent(agentName, businessRulesUserInput);
            setScreenValuesBasedOnResult(result);
        }
    }

    private boolean isUniqueAgentName(String agentName) {
        if (items != null){
            return !items.contains(agentName);
        }

        return true;
    }

    /***
     * Set the button back to have the action to go back to list with agent's
     */
    @FXML
    private void backButtonAction() {
        programAgentList.setupScreen();
    }

    /**
     * Set the onscreen values based on the result from parsing.
     *
     * @param result The result from parsing.
     */
    private void setScreenValuesBasedOnResult(List<UserInputBusinessRule> result) {
        StringBuilder errors = new StringBuilder();
        List<Text> textFlow = new ArrayList<>();
        for (UserInputBusinessRule businessRule : result) {
            Text text = new Text(businessRule.getBusinessRule() + "\n");
            if (!businessRule.getErrorWord().isEmpty()) {
                Integer key = businessRule.getErrorWord().keySet().iterator().next();
                int value = businessRule.getErrorWord().get(key) + 1;
                textFlow.add(new Text(businessRule.getBusinessRule().substring(0, key)));
                Text errorWord = new Text(businessRule.getBusinessRule().substring(key, value));
                errorWord.setFill(Color.RED);
                textFlow.add(errorWord);
                textFlow.add(new Text(businessRule.getBusinessRule().substring(value) + "\n"));
                errors.append(businessRule.getErrorMessage() + "\n");
            } else if (businessRule.hasError()) {
                errors.append("User input error on line ").append(businessRule.getLineNumber()).append(": ").append(businessRule.getErrorMessage()).append("\n");
                text.setFill(Color.RED);
                textFlow.add(text);
            }else{
                textFlow.add(text);
            }
        }
        businessRuleTexFlow.getChildren().addAll(textFlow);
        if (errors.toString().isEmpty()) {
            setProgramAgentPopup(resourceBundle.getString("business_rule_success_header"), resourceBundle.getString("business_rule_success_body"), Color.GREEN);
        } else {
            businessRuleErrorTextArea.setText(errors.toString());
        }
    }

    /**
     * Clears the text from the screen textflow and textarea.
     */
    private void clearOldScreenValues() {
        businessRuleErrorTextArea.clear();
        businessRuleTexFlow.getChildren().clear();
    }

    /***
     * Set the action for the MoreInfo Button.
     * It opens the new info stage.
     */
    @FXML
    private void moreInfoButtonAction() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/ProgramAgentInfo.fxml"), resourceBundle);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private boolean checkIfStringEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public void setItems(ObservableList<String> items) {
        this.items = items;
    }
}
