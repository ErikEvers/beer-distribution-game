package org.han.ica.asd.c.gui_program_agent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProgramAgentListController {
    @FXML
    AnchorPane mainContainer;

    @FXML
    Button edit;

    @FXML
    Button delete;

    @FXML
    ListView list;

    @Inject
    @Named("ProgramAgent")
    IGUIHandler programAgent;

    @Inject
    @Named("MainMenu")
    IGUIHandler mainMenu;

    @Inject
    @Named("BusinessruleStore")
    IBusinessRuleStore iBusinessRuleStore;

    private ObservableList<String> items = FXCollections.observableArrayList();

    public void initialize() {
        list.setItems(items);
        List<String> agents = iBusinessRuleStore.getAllProgrammedAgents();
        if (!agents.isEmpty()) {
            items.addAll(agents);
        }
    }

    @FXML
    private void backButtonAction() {
        mainMenu.setupScreen();
    }

    @FXML
    private void programAgentButtonAction() {
        programAgent.setData(new Object[]{null, items});
        programAgent.setupScreen();
    }

    @FXML
    public void handleMouseClickOnList() {
        Object selectedItem = list.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            delete.setDisable("Default".equals(selectedItem.toString()));
            delete.setVisible(true);
            edit.setVisible(true);
        }
    }

    @FXML
    public void editButtonAction() {
        programAgent.setData(new Object[]{list.getSelectionModel().getSelectedItem(),null});
        programAgent.setupScreen();
    }

    @FXML
    public void deleteButtonAction() {
        Object selectedAgent = list.getSelectionModel().getSelectedItem();

        if ("Default".equalsIgnoreCase(selectedAgent.toString())){
            Alert alert = new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("languageResourcesGuiProgramAgent").getString("delete_default_agent"), ButtonType.CLOSE);
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, ResourceBundle.getBundle("languageResourcesGuiProgramAgent").getString("delete_agent") + selectedAgent.toString() + "?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                items.remove(selectedAgent);
                iBusinessRuleStore.deleteProgrammedAgent(selectedAgent.toString());
            }
        }
    }
}
