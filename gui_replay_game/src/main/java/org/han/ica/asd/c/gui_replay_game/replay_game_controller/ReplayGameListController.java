package org.han.ica.asd.c.gui_replay_game.replay_game_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.interfaces.replay.IRetrieveReplayData;
import org.han.ica.asd.c.model.domain_objects.BeerGame;

import javax.inject.Inject;
import javax.inject.Named;

public class ReplayGameListController {
    @FXML
    AnchorPane mainContainer;

    @FXML
    Button replay;

    @FXML
    ListView list;

    @Inject
    @Named("ReplayGameRound")
    IGUIHandler replayGameRound;

    @Inject
    @Named("MainMenu")
    IGUIHandler mainMenu;

    @Inject
    IRetrieveReplayData retrieveReplayData;


    /***
     * Function for initialising the current ProgramAgentInfo FXML. It also sets the actions of the button to close current window on click.
     */
    public void initialize() {
        ObservableList<BeerGame> items = FXCollections.observableArrayList(retrieveReplayData.getAllBeerGames());

        list.setCellFactory( listView -> {
            TextFieldListCell<BeerGame> cell = new TextFieldListCell<>();
            cell.setConverter(new StringConverter<BeerGame>() {
                @Override
                public String toString(BeerGame beerGame) {
                    return beerGame.getGameName() + " " + beerGame.getGameDate();
                }

                @Override
                public BeerGame fromString(String string) {
                    return null;
                }
            });
            return cell;
        });

        list.setItems(items);
    }

    @FXML
    private void replayButtonAction() {
        if(list.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error selecting game");
            alert.setHeaderText(null);
            alert.setContentText("Please select a valid BeerGame!");

            alert.showAndWait();
        }
        else {
            replayGameRound.setData(new Object[]{list.getSelectionModel().getSelectedItem()});
            replayGameRound.setupScreen();
        }
    }

    @FXML
    private void backButtonAction() {
        mainMenu.setupScreen();
    }

    @FXML
    public void handleMouseClickOnList(MouseEvent mouseEvent) {
        replay.setVisible(true);
    }
}
