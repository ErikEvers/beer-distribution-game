package org.han.ica.asd.c.gui_program_agent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

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
        if(!agents.isEmpty()){
            items.addAll(agents);
        }
    }

    @FXML
    private void backButtonAction() {
        mainMenu.setupScreen();
    }

    @FXML
    private void programAgentButtonAction() {
        programAgent.setupScreen();
    }

    @FXML
    public void handleMouseClickOnList(MouseEvent arg0) {
        if(list.getSelectionModel().getSelectedItem() !=null) {
            edit.setVisible(true);
            delete.setVisible(true);
        }
    }

    @FXML
    public void editButtonAction() {
        programAgent.setData(new Object[]{list.getSelectionModel().getSelectedItem()});
        programAgent.setupScreen();

    }

    @FXML
    public void deleteButtonAction() {
        //TODO INJECT BUSINESS RULE STORE
        items.remove(list.getSelectionModel().getSelectedItem());
        iBusinessRuleStore.deleteProgrammedAgent(list.getSelectionModel().getSelectedItem().toString());
    }
}
