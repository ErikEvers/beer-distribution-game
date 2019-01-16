package org.han.ica.asd.c.gui_play_game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.businessrule.IBusinessRuleStore;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

public class SelectAgentController {
    @FXML
    ListView list;
    @FXML
    Button selectAgentButton;
    @FXML
    Button rejoinGame;
    @FXML
    Label headerText;

    @Inject
    IBusinessRuleStore iBusinessRuleStore;

    @Inject
    @Named("PlayerComponent")
    IPlayerComponent iPlayerComponent;

    @Inject
    @Named("PlayGame")
    IGUIHandler playGameFactory;

    private ObservableList<String> items = FXCollections.observableArrayList();

    public void initialize() {
        list.setItems(items);
        List<String> agents = iBusinessRuleStore.getAllProgrammedAgents();
        if (!agents.isEmpty()) {
            items.addAll(agents);
        }
    }

    @FXML
    public void selectAgentButtonAction() {
        Object selectedAgent = list.getSelectionModel().getSelectedItem();
        iPlayerComponent.activateAgent(iBusinessRuleStore.getProgrammedGameAgent(selectedAgent.toString()));
        headerText.setText("Agent selected: " + selectedAgent.toString());
        list.setVisible(false);
        selectAgentButton.setVisible(false);
        playGameFactory.setData(new Object[]{true});
        playGameFactory.setupScreen();
    }
}