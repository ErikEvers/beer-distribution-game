package org.han.ica.asd.c;




import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;
import org.han.ica.asd.c.interfaces.gui_play_game.IPlayerComponent;
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

    /**
     * superInitialization of the two controller subclasses. Has code needed for both initializations.
     */
    protected void superInitialize() {
        mainContainer.getChildren().addAll();
        playGridPane.setStyle("-fx-border-style: solid inside;" + "-fx-border-color: black;" + "-fx-border-radius: 40;");

        //TODO remove the orderfake class when integrating the game so it is playable.
        orderFake = new OrderFake();

        //TODO remove the facilityFake when the correct facilitielinks is being filled in the comboBox.
        facilityFake = new FacilityFake();

        //Make sure only numbers can be filled in the order textBox. This is done using a textFormatter
        UnaryOperator<TextFormatter.Change> textFieldFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([0-9]*)?")){
                return change;
            }
            return null;
        };

        outgoingOrderTextField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, textFieldFilter));
    }

    /**
     * Calculates inventory.
     * @param ingoingGoods goods the facility is receiving from another facility.
     * @param outgoingGoods goods the facility is giving to another facility.
     * @return the new inventory
     */
    public String calculateInventory(int ingoingGoods, int outgoingGoods){
        //TODO calculation is wrongly stubbed for now... make sure the outcome of the real calculation is being displayed on the screen from the game logic.
        int inventoryValue = Integer.parseInt(inventory.getText());
        int result = (inventoryValue + ingoingGoods) - outgoingGoods;

        return Integer.toString(result);
    }
}
