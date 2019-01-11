package org.han.ica.asd.c.gui_manage_players;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.han.ica.asd.c.fxml_helper.IGUIHandler;
import org.han.ica.asd.c.gameconfiguration.ManagePlayersService;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManagePlayersScreenController  {

	@Inject
	ManagePlayersService gameConfiguration;

	@Inject
	@Named("AssignAgents")
	public IGUIHandler assignAgents;


	@FXML private Button kickPlayerButton;
	@FXML private TableView<Player> playerTable;
	@FXML private TableColumn<Player, String> playerIdColumn;
	@FXML private TableColumn<Player, String> nameColumn;
	@FXML private TableColumn<Player, String> ipColumn;

	private BeerGame beerGame;
	private ResourceBundle resourceBundle;

	/**
	 * Initialize the controller by setting cell value factories for the player table columns,
	 * Retrieve and set the players in the table.
	 */
	public void initialize() {
		this.resourceBundle = ResourceBundle.getBundle("languageResourcesManagePlayers");
		playerIdColumn.setCellValueFactory(new PropertyValueFactory<>("playerId"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		ipColumn.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
	}

	/**
	 * Fill the player table with player records.
	 */
	private void populateTable() {
		playerTable.setItems(FXCollections.observableArrayList(beerGame.getPlayers()));
	}

	/**
	 * Return to the game room
	 */
	public void handleReturnToGameRoomButtonClick() {
		assignAgents.setData(new Object[] { beerGame });
		assignAgents.setupScreen();
	}

	/**
	 * Enable the kick button whenever a player gets selected in the table.
	 */
	public void handlePlayerSelection() {
		if(playerTable.getSelectionModel().getSelectedItem() != null) {
			kickPlayerButton.setDisable(false);
		}
	}

	/**
	 * Process the removal of a player by removing them from the table and passing them to the relevant objects.
	 */
	public void handleKickPlayerButtonClick() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, resourceBundle.getString("kick_player_confirmation") + playerTable.getItems().get(playerTable.getSelectionModel().getFocusedIndex()).getName() + "?");
		alert.setTitle(resourceBundle.getString("kick_player_title"));
		Optional<ButtonType> clicked = alert.showAndWait();

		if (clicked.get() == ButtonType.OK) {
			Player toRemove = playerTable.getItems().get(playerTable.getSelectionModel().getFocusedIndex());
			beerGame = gameConfiguration.removePlayer(beerGame, toRemove);
			populateTable();
		}

	}

	public void setBeerGame(BeerGame beerGame) {
		this.beerGame = beerGame;
		populateTable();
	}
}
