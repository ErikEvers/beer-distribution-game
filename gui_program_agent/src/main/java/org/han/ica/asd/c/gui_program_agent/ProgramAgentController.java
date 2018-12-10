package org.han.ica.asd.c.gui_program_agent;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.han.ica.asd.c.businessrule.BusinessRuleHandler;
import org.han.ica.asd.c.businessrule.IBusinessRules;

import java.io.IOException;

public class ProgramAgentController implements EventHandler<ActionEvent> {

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

    public ProgramAgentController(){
        //TODO vervangen voor inject.
        iBusinessRules = new BusinessRuleHandler();
    }

    public void initialize() {
        mainContainer.getChildren().addAll();
        moreInfo.setOnAction(event -> {
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml/ProgramAgentInfo.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 1000, 800));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        save.setOnAction(event -> {
            String agentName = agentNameInput.getText();
            String businessRules = businessRuleInput.getText();
            if (agentName == null || agentName.trim().isEmpty()) {

            }

            iBusinessRules.programAgent(agentName, businessRules);
        });
    }

    @Override
    public void handle(ActionEvent event) {

    }
}
