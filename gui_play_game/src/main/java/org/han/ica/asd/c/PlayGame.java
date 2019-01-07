package org.han.ica.asd.c;

//*


import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.FacilityType;
import org.han.ica.asd.c.model.Player;
import org.han.ica.asd.c.public_interfaces.IPlayerComponent;


//
public class PlayGame {
    @FXML
    protected GridPane playGridPane;

    @FXML
    protected AnchorPane mainContainer;

    @FXML
    protected Button btnSendTurn;

    @FXML
    protected TextField txtOutgoingOrder;

    @Inject
    @Named("PlayerComponent") protected IPlayerComponent playerComponent;

    protected void superInitialize() {
        FacilityType facilityType = new FacilityType("Retailer", "0", 40, 40, 40 ,40, 40 ,40);
        Facility facility = new Facility("0", 0, facilityType, "0", "0");
       // playerComponent.chooseFacility(facility);

        mainContainer.getChildren().addAll();
        playGridPane.setStyle("-fx-border-style: solid inside;" + "-fx-border-color: black;" + "-fx-border-radius: 40;");
    }

    @FXML
    private void sendOrder(ActionEvent e) {
        int orderAmount = 0;

        try {
            orderAmount = Integer.parseInt(txtOutgoingOrder.getText());
        } catch (NumberFormatException eMessage) {
            System.out.println("NAN");
            txtOutgoingOrder.setText("");
            return;
        }

        //playerComponent.placeOrder(15);
        System.out.println(txtOutgoingOrder.getText());
    }
}
