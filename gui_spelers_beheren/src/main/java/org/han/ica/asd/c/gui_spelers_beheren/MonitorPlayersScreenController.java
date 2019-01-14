package org.han.ica.asd.c.gui_spelers_beheren;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.han.ica.asd.c.gameconfiguration.GameConfiguration;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;

public class MonitorPlayersScreenController {

	@Inject GameConfiguration gameConfiguration;

	@FXML private Button kickPlayerButton;
	@FXML private TableView<Player> playerTable;
	@FXML private TableColumn<Player, String> playerIdColumn;
	@FXML private TableColumn<Player, String> nameColumn;
	@FXML private TableColumn<Player, String> ipColumn;

	/**
	 * Initialize the controller by setting cell value factories for the player table columns,
	 * Retrieve and set the players in the table.
	 */
	public void initialize() {
		playerIdColumn.setCellValueFactory(new PropertyValueFactory<>("playerId"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		ipColumn.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));

		populateTable();
	}

	/**
	 * Fill the player table with player records.
	 */
	private void populateTable() {
		playerTable.setItems(FXCollections.observableArrayList(gameConfiguration.getPlayers()));
	}

	/**
	 * Return to the game room
	 */
	public void handleReturnToGameRoomButtonClick() {
		// coming up
	}

	/**
	 * Start the actual game!
	 */
	public void handleStartGameButtonClick() {
		// coming up
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
		Player toRemove = playerTable.getItems().get(playerTable.getSelectionModel().getFocusedIndex());
		gameConfiguration.removePlayer(toRemove);
		populateTable();
	}
}
