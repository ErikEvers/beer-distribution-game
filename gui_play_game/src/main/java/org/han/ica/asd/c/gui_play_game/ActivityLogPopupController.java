package org.han.ica.asd.c.gui_play_game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.han.ica.asd.c.model.domain_objects.BeerGame;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnDeliver;
import org.han.ica.asd.c.model.domain_objects.FacilityTurnOrder;
import org.han.ica.asd.c.model.domain_objects.Round;
import java.util.stream.Collectors;

public class ActivityLogPopupController {

	@FXML
	AnchorPane mainContainer;

	@FXML
	Button close;

	@FXML
	ListView logTextArea;

	/**
	 *  Function for initialising the current ProgramAgentInfo FXML. It also sets the actions of the button to close current window on click.
	 */
	public void initialize() {
		mainContainer.getChildren().addAll();
		close.setOnAction(event -> {
			Stage stage = (Stage) close.getScene().getWindow();
			stage.close();
		});
	}

	/**
	 * Sets the text of the body label
	 * @param beerGame The object to be parsed for the activity log.
	 * @param facilityId The id of the facility for which the logs should be shown.
	 */
		void setLogContent(BeerGame beerGame, int facilityId) {
			StringBuilder builder = new StringBuilder(200);
			ObservableList<String> items = FXCollections.observableArrayList();
			for(Round round : beerGame.getRounds()) {
				for(FacilityTurnOrder facilityTurnOrder : round.getFacilityOrders().stream().filter(order -> order.getFacilityIdOrderTo() == facilityId).collect(Collectors.toList())) {
					builder.append("Round ");
					builder.append(round.getRoundId());
					builder.append(": ");
					builder.append("Facility ");
					builder.append(facilityTurnOrder.getFacilityId());
					builder.append(" ordered ");
					builder.append(facilityTurnOrder.getOrderAmount());
					builder.append(" pc from you");
					items.add(builder.toString());
					builder = new StringBuilder();
				}

				for(FacilityTurnDeliver facilityTurnDeliver : round.getFacilityTurnDelivers().stream().filter(order -> order.getFacilityIdDeliverTo() == facilityId).collect(Collectors.toList())) {
					builder.append("Round ");
					builder.append(round.getRoundId());
					builder.append(": ");
					builder.append("Facility ");
					builder.append(facilityTurnDeliver.getFacilityId());
					builder.append(" delivered ");
					builder.append(facilityTurnDeliver.getDeliverAmount());
					builder.append(" pc to you");
					items.add(builder.toString());
					builder = new StringBuilder();
				}

				for(FacilityTurnDeliver facilityTurnDeliver : round.getFacilityTurnDelivers().stream().filter(order -> order.getFacilityId() == facilityId).collect(Collectors.toList())) {
					builder.append("Round ");
					builder.append(round.getRoundId());
					builder.append(": ");
					builder.append("You delivered ");
					builder.append(facilityTurnDeliver.getDeliverAmount());
					builder.append(" pc to facility ");
					builder.append(facilityTurnDeliver.getFacilityId());
					items.add(builder.toString());
					builder = new StringBuilder();
				}

				for(FacilityTurnOrder facilityTurnOrder : round.getFacilityOrders().stream().filter(order -> order.getFacilityId() == facilityId).collect(Collectors.toList())) {
					builder.append("Round ");
					builder.append(round.getRoundId());
					builder.append(": ");
					builder.append("You ordered ");
					builder.append(facilityTurnOrder.getOrderAmount());
					builder.append(" pc from facility ");
					builder.append(facilityTurnOrder.getFacilityId());
					items.add(builder.toString());
					builder = new StringBuilder();
				}
			}
			logTextArea.setItems(items);
		}

}
