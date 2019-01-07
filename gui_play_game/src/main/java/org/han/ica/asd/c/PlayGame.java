package org.han.ica.asd.c;




import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;

/*
import org.han.ica.asd.c.model.Facility;
import org.han.ica.asd.c.model.FacilityType;
import org.han.ica.asd.c.model.Player;
import org.han.ica.asd.c.public_interfaces.IPlayerComponent;
*/
import java.util.function.UnaryOperator;



public class PlayGame {
    @FXML
    private GridPane playGridPane;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    protected TextField incomingOrderTextField;

    @FXML
    protected TextField outgoingOrderTextField;

    @FXML
    protected TextField outgoingGoodsNextRound;

    @FXML
    protected Label inventory;

    protected FacilityFake facilityFake;

    protected OrderFake orderFake;

    protected int roundNumber = 0;


    @Inject
    @Named("PlayerComponent") protected IPlayerComponent playerComponent;

    protected void superInitialize() {

      //  mainContainer.getChildren().addAll();
        playGridPane.setStyle("-fx-border-style: solid inside;" + "-fx-border-color: black;" + "-fx-border-radius: 40;");
        incomingOrderTextField.setText("50");

        orderFake = new OrderFake();
        facilityFake = new FacilityFake();

        UnaryOperator<TextFormatter.Change> textFieldFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([0-9]*)?")){
                return change;
            }
            return null;
        };

        outgoingOrderTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, textFieldFilter));
    }

    //TODO calculation is wrongly stubbed for now... make sure the outcome of the real calculation is being displayed on the screen.
    public String calculateInventory(int ingoingGoods, int outgoingGoods){
        int inventoryValue = Integer.parseInt(inventory.getText());
        int result = (inventoryValue + ingoingGoods) - outgoingGoods;

        return Integer.toString(result);
    }
}
